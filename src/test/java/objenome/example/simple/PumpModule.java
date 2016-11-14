package objenome.example.simple;

import objenome.the;
import objenome.out;

@the(complete = false, library = true)
class PumpModule {
  @out Pump providePump(Thermosiphon pump) {
    return pump;
  }
}
