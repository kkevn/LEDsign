/**
 * This file contains the Arduino sketch for rendering the effects received from Bluetooth. The
 * sketch will intialize the circuit and wait for an incoming BlueTooth connection. Upon
 * establishing this connection, the sketch will then wait for incoming data via BlueTooth. The
 * data expected must be in a specific format that the accompanying Android application LEDsign
 * outputs in. Once this data is received, the sketch will decode the data and interpret it into
 * a collection of effects to be rendered on some given combination of the LED matrices attached
 * to the Arduino device.
 *
 * @author Kevin Kowalski
 */

#include <avr/wdt.h>
#include <Adafruit_GFX.h>
#include <Adafruit_NeoMatrix.h>
#include <Adafruit_NeoPixel.h>
#include <SoftwareSerial.h>

#ifndef PSTR
  #define PSTR // used for Arduino Due
#endif

#define PIN 6
#define CHAR_WIDTH 6
#define PANEL_COUNT 5
#define PANEL_WIDTH 8
#define PANEL_HEIGHT 8

// initialize the daisy-chained matrix panels
Adafruit_NeoMatrix matrix = Adafruit_NeoMatrix(PANEL_WIDTH, PANEL_HEIGHT, PANEL_COUNT, 1, PIN,
                            NEO_TILE_TOP   + NEO_TILE_LEFT    + NEO_TILE_ROWS   + NEO_TILE_ZIGZAG +
                            NEO_MATRIX_TOP + NEO_MATRIX_RIGHT + NEO_MATRIX_ROWS + NEO_MATRIX_ZIGZAG,
                            NEO_GRB + NEO_KHZ800);

// pins connected with BlueTooth module
SoftwareSerial BT(2, 3);

/**
 * Runs once prior to running the sketch. Initializes data rates and NeoMatrix object.
 */
void setup() {
  Serial.begin(9600);

  // start the NeoMatrix object at cap the brightness
  matrix.begin();
  matrix.setTextWrap(false);
  matrix.setBrightness(8);

  // set the data rate for the SoftwareSerial port
  BT.begin(9600);
}

// BlueTooth data decoding variables
char bt_input; // stores incoming character from other device
char bt_data[256];  // stores just effects from bt_input
int bt_i = 0;
int COUNTER = 0;
char* split_effects[1]; // contains array of each effect parsed
boolean ended = false;

// debug flag
boolean DEBUG = true;

// idle animation variables
int idler = 1;
int text_x = (PANEL_WIDTH * 1) - CHAR_WIDTH;

/**
 * Runs the sketch. Handles all decoding of BlueTooth input and rendering of the matrices. Expected
 * format of incoming BlueTooth data is:
 *    <[Effect abbreviation]{xxxxx;y;y;y;z;},[Effect abbreviation]{xxxxx;y;y;y;z;},...,>
 * The effect abbreviaton does not actually include square brackets, yet the parameters for said effect
 * are indeed contained within curly braces. The series of `x` variables are the flags for which matrices 
 * to apply the effect to, with `y` and `z` as miscellaneous parameter examples. Each effect is comma 
 * separated (even the last effect) and the entire incoming string must be enclosed in the angle brackets. 
 * An actual example might look like:
 *    <SC{10110;37;255;0;},ST{11111;Hello World;219;0;197;},F{00001;},>
 * 
 * Additionally, supposedly the Arduino cannot accept BlueTooth data larger than ~64 characters at a time
 * and so the accompanying Android application sends data one effect at a time, where each effect never 
 * contains more than 64 characters in its parsed form. Sending the data in one go over ~64 characters will
 * result in the Arduino hanging. This took me quite some time to discover.
 */
void loop() {

  // clear the matrices between each frame
  matrix.clear();
  matrix.fillScreen(0);

  // BlueTooth input blank, play idle animation
  if (bt_data[0] == '\0') {
    idleAnimation();
    idler++;
    if (idler > 5)
      idler = 1;
  }

  // read any incoming BlueTooth data
  while (BT.available()) {

    // get the current character
    bt_input = (BT.read());

    // character is not start or end of data i.e. still parsing input
    if (bt_input != '>' && bt_input != '<') {

      // end of effect reached, update effect counter
      if (bt_input == ',')
        COUNTER++;

      // update bt_data with current effect input
      bt_data[bt_i] = bt_input;
      bt_i++;
      Serial.println(bt_data);
    }
    
    // start of new input detected with '<'
    else if (bt_input == '<') {
      COUNTER = 0;
      ended = false;
      clearInput();
      if (DEBUG)
        Serial.println(F("\n\t<=== Input Start"));
    }
    
    // reached end of input with '>'
    else {

      // terminate bt_data
      bt_data[bt_i] = '\0';
      bt_i = 0;
      ended = true;

      // split effects into array based on how many there were
      split_effects[COUNTER];
      split(split_effects, COUNTER, bt_data, ",");

      if (DEBUG)
        Serial.println(F("\n\tInput End ===>"));
    }
  }

  // only render effects if an input was fully parsed
  if (ended == true) {

    // print out some variables for debugging purposes
    if (DEBUG) {
      Serial.println(F("\n--------------\n"));
      Serial.println(F("\nbt_data:"));
      Serial.println(bt_data);
      Serial.println(strlen(bt_data));
    }

    if (DEBUG) {
      Serial.println(F("\nCounter="));
      Serial.println(COUNTER);
    }

    // iterate over each effect in list of effects
    for (int i = 0; i < COUNTER; i++) {

      if (DEBUG) {
        Serial.println(F("\ni="));
        Serial.println(i);
      }

      // get current effect
      char* effect = split_effects[i];
      if (DEBUG) {
        Serial.println(F("\nEffect:"));
        Serial.println(effect);
      }

      // get effect type
      int i_start = 0;
      int i_end = getIndexOf(effect, '{');
      char effect_type[i_end - i_start + 1];
      getSubstringOf(effect_type, effect, i_start, i_end);

      // get effect parameter list
      i_start = getIndexOf(effect, '{') + 1;
      i_end = strlen(effect) - 1;
      char effect_params[i_end - i_start + 1];
      getSubstringOf(effect_params, effect, i_start, i_end);

      // split parameter list into array
      int param_count = countInput(effect_params, ';') + 1;
      char* params[param_count];
      split(params, param_count, effect_params, ";");

      // determine effect to show
      if (strcmp(effect_type, "ST") == 0) {
        Serial.println(F("\nScrolling Text"));
        scrollText(params[0], params[1], atoi(params[2]), atoi(params[3]), atoi(params[4]));
      } else if (strcmp(effect_type, "SC") == 0) {
        Serial.println(F("\nSolid Color"));
        solidColor(params[0], atoi(params[1]), atoi(params[2]), atoi(params[3]));
      } else if (strcmp(effect_type, "RW") == 0) {
        Serial.println(F("\nRainbow Wave"));
        rainbow(params[0]);
      } else if (strcmp(effect_type, "F") == 0) {
        Serial.println(F("\nFade"));
        fade(params[0]);
      } else if (strcmp(effect_type, "CW") == 0) {
        Serial.println(F("\nColor Wipe"));
        wipe(params[0], atoi(params[1]), atoi(params[2]), atoi(params[3]), atoi(params[4]));
      } else if (strcmp(effect_type, "TC") == 0) {
        Serial.println(F("\nTheater Chase"));
        theaterChase(params[0], atoi(params[1]), atoi(params[2]), atoi(params[3]), atoi(params[4]));
      } else if (strcmp(effect_type, "RESET") == 0) {
        Serial.println(F("\nRESET"));
        softwareReset(WDTO_60MS);
      } else {
        Serial.println(F("\n*!* Unknown"));
      }
    }
  }

  // update the current frame and wait a bit before refreshing
  matrix.show();
  delay(100);
}

/* HELPER FUNCTIONS */

/**
 * Starts a system reset for the Arduino by activating watchdog with given prescaller. Waits for
 * the prescaller time to expire without send the reset signal by using the wdt_reset() method.
 * 
 * @param {uint8_t} prescaller: Time to wait before resetting.
 * 
 * @source https://www.codeproject.com/articles/1012319/arduino-software-reset
 */
void softwareReset(uint8_t prescaller) {
  wdt_enable(prescaller);
  while (1) {}
}

/**
 * Clears the parsed data stored in bt_data and its index counter.
 */
void clearInput() {
  for (int i = 0; i < strlen(bt_data); i++) {
    bt_data[i] = 0;
  }
  bt_i = 0;
}

/**
 * Custom chatAt() method to fetch the index of the given character in the given array.
 * 
 * @param {char*} input: Input to search in.
 * @param {char} c: Character to find.
 * 
 * @return {int} Index of character in input.
 */
int getIndexOf(char* input, char c) {

  int indexAt = -1;

  for (int i = 0; i < strlen(input); i++) {
    if (input[i] == c) {
      indexAt = i;
    }
  }

  return indexAt;
}

/**
 * Custom substring() method to fetch the substring of the input to the result variable between the
 * given indexes.
 * 
 * @param {char*} result: Array to store input in.
 * @param {char*} input: Input to obtain result from.
 * @param {int} idx_start: Starting index.
 * @param {int} idx_end: Ending index.
 */
void getSubstringOf(char* result, char* input, int idx_start, int idx_end) {

  int len = idx_end - idx_start;

  for (int i = 0; i < len; i++) {
    result[i] = input[idx_start + i];
  }

  result[len] = '\0';
}

/**
 * Custom split() method that tokenizes the input on the delimiter and parses it into the arr 
 * variable.
 * 
 * @param {char* []} arr: Array to store input in.
 * @param {int} arr_size: Size of expected resulting array.
 * @param {char []} input: Input to obtain result from.
 * @param {char*} delimiter: Delimiter to split on.
 */
void split(char* arr[], int arr_size, char input[], const char* delimiter) {
  char* ptr = strtok(input, delimiter);   // look for first delim
  arr[0] = ptr;
  for (int i = 1; i < arr_size; i++) {
    ptr = strtok(NULL, delimiter);   // look for delim
    arr[i] = ptr;
  }
}

/**
 * Counts the occurrences of the given character in the given input.
 * 
 * @param {char*} input: Input to search in.
 * @param {char} delim: Character to find.
 * 
 * @return {int} Count of delim in input.
 */
int countInput(char* input, char delim) {

  int count = 0;

  for (int i = 0; i < strlen(input); i++) {
    if (input[i] == delim) {
      count++;
    }
  }

  return count;
}

/**
 * Counts the number of enabled matrices in the given selection input.
 * 
 * @param {char*} selections: Input to search in.
 * 
 * @return {int} Count of selections.
 */
int countSelections(char* selections) {

  int count = 0;

  for (int i = 0; i < strlen(selections); i++) {
    if (selections[i] == '1') {
      count++;
    }
  }

  return count;
}

/**
 * Returns index of the first enabled matrix in the given selection input.
 * 
 * @param {char*} selections: Input to search in.
 * 
 * @return {int} First index of a selection.
 */
int firstSelection(char* selections) {

  int index = -1;

  for (int i = 0; i < strlen(selections); i++) {
    if (selections[i] == '1') {
      index = i;
      break;
    }
  }

  return index;
}

/* EFFECT FUNCTIONS */

/**
 * Contains animation logic for the idle state of the Arduino when it is waiting for a BlueTooth 
 * connection. Dances 4 blue and white LEDs in a square pattern on each matrix face.
 */
void idleAnimation() {

  for (int i = 0; i < matrix.width(); i++) {
    matrix.drawPixel(idler + (matrix.width() - (PANEL_WIDTH * i)), 1, matrix.Color(64, 128, 196));
    matrix.drawPixel(6 + (matrix.width() - (PANEL_WIDTH * i)), idler, matrix.Color(196, 196, 196));
    matrix.drawPixel(6 - idler + (matrix.width() - (PANEL_WIDTH * i)), 6, matrix.Color(64, 128, 196));
    matrix.drawPixel(1 + (matrix.width() - (PANEL_WIDTH * i)), 6 - idler, matrix.Color(196, 196, 196));
  }
}

/**
 * Custom animation logic for rendering a single static color to the specified matrices.
 * 
 * @param {char*} selections: Matrix selections to render on.
 * @param {int} r: Desired red RGB value.
 * @param {int} g: Desired green RGB value.
 * @param {int} b: Desired blue RGB value.
 */
void solidColor(char* selections, int r, int g, int b) {

  for (int i = 0; i < PANEL_COUNT; i++) {
    if (selections[i] == '1') {
      for (int x = (PANEL_WIDTH * i); x < (PANEL_WIDTH * (i + 1)); x++) {
        for (int y = 0; y < PANEL_HEIGHT; y++) {
          matrix.drawPixel(x, y, matrix.Color(r, g, b));
        }
      }
    }
  }
}

/**
 * Custom animation logic for rendering a theater chase effect to the given matrices. Can use 
 * either a given static color or iterate through the RGB color space.
 * 
 * @param {char*} selections: Matrix selections to render on.
 * @param {int} r: Desired red RGB value.
 * @param {int} g: Desired green RGB value.
 * @param {int} b: Desired blue RGB value.
 * @param {boolean} doRainbow: Whether or not to use rainbow coloring.
 */
void theaterChase(char* selections, int r, int g, int b, boolean doRainbow) {

  int firstPixelHue = 0;
  
  for (int a = 0; a < 30; a++) {
    for (int b = 0; b < 3; b++) {

      // set all pixels in RAM to 0 (off)
      matrix.clear();
      
      for (int i = 0; i < PANEL_COUNT; i++) {
        if (selections[i] == '1') {
          int panelSize = PANEL_WIDTH * PANEL_HEIGHT;

          // 'c' counts up from 'b' to end of strip in steps of 3...
          for (int j = (i * panelSize) + b; j < (i + 1) * panelSize; j += 3) {

            // update the LED at current position with a color
            if (doRainbow) {
              int hue = firstPixelHue + j * 65536L / matrix.numPixels();
              uint32_t color = matrix.gamma32(matrix.ColorHSV(hue));
              matrix.setPixelColor(j, color);
            } else {
              matrix.setPixelColor(j, r, g, b);
            }
          }
        }
      }
      
      // update for new contents and wait a bit
      matrix.show();
      delay(64);

      // do one cycle of color wheel over 90 frames
      if (doRainbow)
        firstPixelHue += 65536 / 90;
    }
  }
}

/**
 * Custom animation logic for rendering a color wipe effect to the given matrices. Can run up 
 * to 'runs' amount of variations of it.
 * 
 * @param {char*} selections: Matrix selections to render on.
 * @param {int} r: Desired red RGB value.
 * @param {int} g: Desired green RGB value.
 * @param {int} b: Desired blue RGB value.
 * @param {int} runs: How many color variations to run.
 */
void wipe(char* selections, int r, int g, int b, int runs) {
  //for (int k = 0; k < runs; k++) {
    for (int i = 0; i < PANEL_COUNT; i++) {
      if (selections[i] == '1') {
        int panelSize = PANEL_WIDTH * PANEL_HEIGHT;
        for (int j = i * panelSize; j < (i + 1) * panelSize; j++) {
          //for (int k = 0; k < runs; k++) {
            /*if (k == 0)
                matrix.setPixelColor(j, r, g, b);
              else if (k == 1)
                matrix.setPixelColor(j, g, r, g);
              else if (k == 2)
                matrix.setPixelColor(j, b, g, r);*/
          //}
          matrix.setPixelColor(j, r, g, b);
          matrix.show();
          delay(50);
          //}
        }
      }
    }
  //}
}

/**
 * First revision of color wipe effect; this version has a snake-game-like effect.
 * 
 * @param {char*} selections: Matrix selections to render on.
 * @param {int} r: Desired red RGB value.
 * @param {int} g: Desired green RGB value.
 * @param {int} b: Desired blue RGB value.
 * @param {int} runs: How many color variations to run.
 */
void wipe2(char* selections, int r, int g, int b, int runs) {

  for (int i = 0; i < PANEL_COUNT; i++) {
    if (selections[i] == '1') {
      int panelSize = PANEL_WIDTH * PANEL_HEIGHT;
      for (int j = i * panelSize; j < (i + 1) * panelSize; j++) {
        for (int k = 0; k < runs; k++) {
          if (k == 0)
            matrix.setPixelColor(j, r, g, b);
          else if (k == 1)
            matrix.setPixelColor(j, g, r, g);
          else if (k == 2)
            matrix.setPixelColor(j, b, g, r);
          matrix.show();
          delay(50);
        }
      }
    }
  }
}

/**
 * Custom animation logic for rendering a rainbow wave effect to the given matrices.
 * 
 * @param {char*} selections: Matrix selections to render on.
 */
void rainbow(char* selections) {

  for (long firstPixelHue = 0; firstPixelHue < 5 * 65536; firstPixelHue += 256) {
    for (int i = 0; i < PANEL_COUNT; i++) {
      if (selections[i] == '1') {
        int panelSize = PANEL_WIDTH * PANEL_HEIGHT;
        for (int j = i * panelSize; j < (i + 1) * panelSize; j++) {
          int pixelHue = firstPixelHue + (j * 65536L / matrix.numPixels());
          matrix.setPixelColor(j, matrix.gamma32(matrix.ColorHSV(pixelHue)));
        }
        matrix.show();
      }
    }
  }
}

/**
 * Custom animation logic for rendering a fade effect to the given matrices.
 * 
 * @param {char*} selections: Matrix selections to render on.
 */
void fade(char* selections) {

  for (long firstPixelHue = 0; firstPixelHue < 5 * 65536; firstPixelHue += 256) {
    for (int i = 0; i < PANEL_COUNT; i++) {
      if (selections[i] == '1') {
        int panelSize = PANEL_WIDTH * PANEL_HEIGHT;
        for (int j = i * panelSize; j < (i + 1) * panelSize; j++) {
          matrix.setPixelColor(j, matrix.gamma32(matrix.ColorHSV(firstPixelHue)));
        }
        matrix.show();
      }
    }
  }
}

/**
 * Custom animation logic for rendering a text scroll effect to the given matrices.
 * 
 * @param {char*} selections: Matrix selections to render on.
 * @param {int} r: Desired red RGB value.
 * @param {int} g: Desired green RGB value.
 * @param {int} b: Desired blue RGB value.
 */
void scrollText(char* selections, char* text, int r, int g, int b) {

  // figure out how many matrices will display this effect
  int matrix_count = countSelections(selections);

  // figure out where to start first letter
  int max_distance = (strlen(text) * CHAR_WIDTH) + (matrix.width() / PANEL_COUNT);

  // figure out how many characters of the given text to render given current position in matrix
  int showCount = map(text_x, -max_distance / 2, (PANEL_WIDTH * matrix_count) - CHAR_WIDTH, strlen(text), 0);

  // draw the substring of text that can fit on the matrix at the calculated positions
  matrix.setCursor(text_x + (firstSelection(selections) * PANEL_WIDTH), 0);
  matrix.setTextColor(matrix.Color(r, g, b));
  char temp[showCount];
  getSubstringOf(temp, text, 0, showCount);
  matrix.print(temp);

  // when enough chars have passed, reset to beginning
  if (--text_x < -max_distance) {
    //text_x = matrix.width();
    text_x = (PANEL_WIDTH * matrix_count) - CHAR_WIDTH;
  }
}
