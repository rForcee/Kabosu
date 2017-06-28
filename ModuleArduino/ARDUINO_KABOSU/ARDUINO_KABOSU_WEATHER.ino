// RAINY = 0;
// CLOUDY = 1;
// SUNNY = 2;
// HEATWAVE = 3;
// THUNDERSTORM = 4;

int current_weather, forecast_weather, proba, probabis, cw, fw;

typedef enum {
  WEATHER_IDLE,
  WEATHER_WEATHER
} WEATHER_STATE;

WEATHER_STATE WEATHERState;

void WEATHER_init() {
  randomSeed(analogRead(5));
  current_weather = random(0, 5);
  forecast_weather = random (0, 5);
}

void WEATHER_update() {
  WEATHER_STATE nextState = WEATHERState;

  switch (WEATHERState) {
    case WEATHER_IDLE :
      if (hour % 24 == 18) {                              // définition de l'heure pour forcast_weather
        nextState = WEATHER_WEATHER;
      }
      break;

    case WEATHER_WEATHER :
      nextState = WEATHER_IDLE;
      break;
  }
}

void WEATHER_output() {
  switch (WEATHERState) {
    case WEATHER_IDLE :
      break;

    case WEATHER_WEATHER :
      current_weather = current(forecast_weather);
      forecast_weather = forecast();
      break;
  }
}

int current(int fw) {
  probabis = random(0, 100);
  if (probabis < 88) {                                        // 88%
    cw = fw;
  }
  if (probabis >= 88 && probabis < 93) {                      // -10%
    cw = ((fw + 5 - 1) % 5);
  }
  if (probabis >= 93 && probabis < 98) {                      // +10%
    cw = ((fw + 5 + 1) % 5);
  }
  else {                                                      // 2%
    cw = forecast();
  }
  return cw;
}

int forecast() {
  proba = random (0, 100);
  if (proba < 15) {
    forecast_weather = 0;
  }
  if (proba >= 15 && proba < 35) {
    forecast_weather = 1;
  }
  if (proba >= 35 && proba < 75) {
    forecast_weather = 2;
  }
  if (proba >= 75 && proba < 95) {
    forecast_weather = 3;
  }
  else {
    forecast_weather = 4;
  }
  return forecast_weather;
}
