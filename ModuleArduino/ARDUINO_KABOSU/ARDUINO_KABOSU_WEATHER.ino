typedef enum {
  WEATHER_IDLE,
  WEATHER_WEATHER
} WEATHER_STATE;

WEATHER_STATE WEATHERState;

void WEATHER_init() {
  gen_weather = false;
  randomSeed(analogRead(5));
  current_weather = random(0, 5);
  forecast_weather = random (0, 5);
}

void WEATHER_update() {
  WEATHER_STATE nextState = WEATHERState;

  switch (WEATHERState) {
    case WEATHER_IDLE :
      if (hour % 24 == 23 && !gen_weather) {                              // définition de l'heure pour forcast_weather
        gen_weather = true;
        nextState = WEATHER_WEATHER;
      }
      if (hour % 24 == 0 && gen_weather) {
        gen_weather = false;
      }
      break;

    case WEATHER_WEATHER :
      current_weather = current(forecast_weather);
      forecast_weather = forecast();
      Serial.print("probaf : ");
      Serial.print(proba);
      Serial.print(" ----- probabisc : ");
      Serial.println(probabis);
      nextState = WEATHER_IDLE;
      break;
  }
  WEATHERState = nextState;
}

void WEATHER_output() {
  switch (WEATHERState) {
    case WEATHER_IDLE :
      break;

    case WEATHER_WEATHER :

      break;
  }
}

int current(int fw) {
  probabis = random(0, 100);
  if (probabis < 88) {                                        // 88%
    return  fw;
  }
  if (probabis >= 88 && probabis < 93) {                      // -10%
    return  ((fw + 5 - 1) % 5);
  }
  if (probabis >= 93 && probabis < 98) {                      // +10%
    return  ((fw + 5 + 1) % 5);
  }
  else {                                                      // 2%
    return  forecast();
  }
}

int forecast() {
  proba = random (0, 100);
  if (proba < 15) {
    return 0;
  }
  if (proba >= 15 && proba < 35) {
    return 1;
  }
  if (proba >= 35 && proba < 75) {
    return 2;
  }
  if (proba >= 75 && proba < 95) {
    return 3;
  }
  else {
    return 4;
  }
}
