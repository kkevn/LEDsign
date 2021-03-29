# LEDsign <img src="https://github.com/kkevn/LEDsign/blob/master/_misc/logo.png" width=7% height=7%>
 
LEDsign is a project designed to perform a very specific goal: allowing a user to build a list of customizable effects using the LEDsign Android application. Next, the user can decide to upload this list of effects (called a profile) to an Arduino circuit via Bluetooth. This circuit will then render the effects designed in the application.

The purpose of this project may seem very niche and that is because it was created entirely with the intention of being a learning exercise and as a fun side project rather than a useful tool of some sort.

See the contents of the `\_misc\` folder for images of the hardware side of the project including the circuit, 3D model of frame built with K'Nex, and more.

---

### Demonstration

<a href="http://www.youtube.com/watch?feature=player_embedded&v=mtEeOngl4wU
" target="_blank"><img src="http://img.youtube.com/vi/mtEeOngl4wU/0.jpg" 
alt="LEDsign Demo" width="640" height="360" border="10" /></a>

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
    * *GNU Lesser General Public License v2.1*
  * [Gson](https://github.com/google/gson)
    * *Apache License 2.0*
  * [Android Support Libraries](https://developer.android.com/topic/libraries/support-library)
    * *Apache License 2.0*
  * [Material Navigation Drawer](https://github.com/kanytu/Android-studio-material-template)
    * *Apache License 2.0*
  * [ExpandableTextView](https://github.com/Manabu-GT/ExpandableTextView)
    * *Apache License 2.0*
  * [SwipeRevealLayout](https://github.com/chthai64/SwipeRevealLayout)
    * *MIT License*

* #### Arduino
  * [Adafruit NeoPixel](https://github.com/adafruit/Adafruit_NeoPixel)
    * *GNU Lesser General Public License v3.0*
  * [Adafruit NeoMatrix](https://github.com/adafruit/Adafruit_NeoMatrix)
    * *GNU General Public License v3.0*
  * [Adafruit GFX](https://github.com/adafruit/Adafruit-GFX-Library)
    * *BSD License*

---

### Specifications

* **Android/Java** for application development *(supports Android 9.0+)*
* **Processing** for application's 3D sketch
* **Arduino Uno** for hardware support
* **BlueTooth** for application/Arduino communication

---

### License

```
MIT License

Copyright (c) 2021 Kevin Kowalski

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

#### Note
This repository depends on several external libraries (but does not modify them). See the [External Libraries](https://github.com/kkevn/LEDsign#external-libraries) section of this README to view their respective licenses and repositories.
