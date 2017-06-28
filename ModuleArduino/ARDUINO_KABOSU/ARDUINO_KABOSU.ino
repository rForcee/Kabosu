#include <LiquidCrystal.h>

LiquidCrystal lcd(8, 9, 4, 5, 6, 7);

long Speed;  // Vitesse = heure réel en milliseconde
long hour, chrono;
int current_weather, forecast_weather, proba, probabis, cw, fw;


//déclaration des fonctions
void CLOCK_init();
void WEATHER_init();
void CLOCK_update();
void WEATHER_update();
void CLOCK_output();
void WEATHER_output();
void send_hour();
int current(int);
int forecast();

void setup() {
  Serial.begin(9600);
  CLOCK_init();
  WEATHER_init();
}

void loop() {
  CLOCK_update();
  WEATHER_update();
  CLOCK_output();
  WEATHER_output();
}
