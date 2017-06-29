#include <LiquidCrystal.h>

LiquidCrystal lcd(8, 9, 4, 5, 6, 7);


long Speed;  // Speed = heure réel en milliseconde
long hour, chrono;
int current_weather, forecast_weather, proba, probabis, cw, fw, tmp, button;
boolean state_button, gen_weather;

//déclaration de toues les fonctions
void CLOCK_init();
void WEATHER_init();
void CLICK_init();
void CLOCK_update();
void WEATHER_update();
void CLICK_update();
void CLOCK_output();
void WEATHER_output();
void CLICK_output();
void Send();
int current(int);
int forecast();


void setup() {
  Serial.begin(9600);
  lcd.begin(16, 2);
  lcd.setCursor(0, 0);
  CLOCK_init();
  WEATHER_init();
  CLICK_init();
}

void loop() {
  CLOCK_update();
  WEATHER_update();
  CLICK_update();
  CLOCK_output();
  WEATHER_output();
  CLICK_output();
}
