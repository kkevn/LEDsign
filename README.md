# LEDsign
 
LEDsign is a project designed to perform a very specific goal: allowing a user to build a list of customizable effects using the LEDsign Android application. Next, the user can decide to upload this list of effects (called a profile) to an Arduino circuit via Bluetooth. This circuit will then render the effects designed in the application.

The purpose of this project may seem very niche and that is because it was created entirely with the intention of being a learning exercise and as a fun side project rather than a useful tool of some sort.

---

### Demonstration

`// todo - insert demo video`

---

### Features

* Easy-to-use effects list customizer
  * drag effects to reposition them in the list
  * modify, duplicate or delete effects
* Instant effect previewing
  * 3D preview automatically updates when effects are added, modified, or removed
* Touch-support on 3D preview
  * swipe to rotate the preview
  * tap HUD elements for quick actions
* Effects profile saving/loading
  * saves to internal storage
  * modify, duplicate or delete existing profiles
* Quick BlueTooth connecting for paired devices
* Profile uploading to Arduino
* Customizable preferences
  * change default profile names
  * enable day/night theme
  * selectable app-wide accent color
  * modify 3D preview preferences
* Help pages with topics that can be swiped to reach next one
  * built-in video instructions
  * additional written instructions
* About page with app external library information
  * tap an item to browse to its home URL

---

### External Libraries

* #### Android
  * [Processing for Android](https://android.processing.org/)
  * [Gson](https://github.com/google/gson)
  * [Android Support Libraries](https://developer.android.com/topic/libraries/support-library)
  * [Material Navigation Drawer](https://github.com/kanytu/Android-studio-material-template)
  * [ExpandableTextView](https://github.com/Manabu-GT/ExpandableTextView)
  * [SwipeRevealLayout](https://github.com/chthai64/SwipeRevealLayout)


* #### Arduino
  * [Adafruit NeoPixel](https://github.com/adafruit/Adafruit_NeoPixel)
  * [Adafruit NeoMatrix](https://github.com/adafruit/Adafruit_NeoMatrix)
  * [Adafruit GFX](https://github.com/adafruit/Adafruit-GFX-Library)

---

### Specifications

* **Android/Java** for application development *(supports Android 9.0+)*
* **Processing** for application's 3D sketch
* **Arduino Uno** for hardware support
* **BlueTooth** for application/Arduino communication
