typedef enum {
  CLOCK_IDLE,
  CLOCK_HOUR
} CLOCK_STATE;

CLOCK_STATE CLOCKState;

void CLOCK_init()
{
  CLOCKState = CLOCK_IDLE;
  hour = 0;
  Speed = 7500;
  chrono = millis();
}


void CLOCK_update() {
  CLOCK_STATE nextState = CLOCKState;

  switch (CLOCKState) {
    case CLOCK_IDLE:
      if (millis() - chrono >= Speed) {
        nextState = CLOCK_HOUR;
      }
      break;

    case CLOCK_HOUR:
      nextState = CLOCK_IDLE;
      break;
  }
  CLOCKState = nextState;
}

void CLOCK_output() {
  switch (CLOCKState)
  {
    case CLOCK_IDLE :
      break;

    case CLOCK_HOUR :
      send_hour();
      break;

  }
}


void send_hour() {
  hour++;
  chrono = millis();
  Serial.println((String)"<{" + hour + "|"/* + current_weather + "|" + forecast_weather + "}>"*/);
}
