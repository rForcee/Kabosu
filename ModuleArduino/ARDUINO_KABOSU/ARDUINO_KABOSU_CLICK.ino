int read_LCD_buttons(){               // read the buttons
    adc_key_in = analogRead(0);       // read the value from the sensor 

    if (adc_key_in > 1000) return btnNONE; 

    // For V1.1 us this threshold
    if (adc_key_in < 50)   return btnRIGHT;  
    if (adc_key_in < 250)  return btnUP; 
    if (adc_key_in < 450)  return btnDOWN; 
    if (adc_key_in < 650)  return btnLEFT; 
    if (adc_key_in < 850)  return btnSELECT;  
    return btnNONE;
}

typedef enum {
  CLICK_IDLE,
  CLICK_HOUR
} CLICK_STATE;

CLICK_STATE CLOCKState;

void CLICK_init()
{

}

void CLICK_update() {
  lcd_key = read_LCD_buttons();
  CLICK_STATE nextState = lcd_key;
  switch (lcd_key) {              // depending on which button was pushed, we perform an action

    case btnRIGHT: {            //  push button "RIGHT" and show the word on the screen

        break;
      }
    case btnLEFT: {

        break;
      }
  }
}
