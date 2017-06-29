typedef enum {
  CLICK_IDLE,
  CLICK_INCREASE,
  CLICK_DECREASE
} CLICK_STATE;

CLICK_STATE CLICKState;

void CLICK_init()
{
  pinMode(button, INPUT);
  tmp = 0;
  state_button = false;
}

void CLICK_update() {
  button = analogRead(A0);
  state_button = digitalRead(button);
  CLICK_STATE nextState = CLICKState;
  switch (CLICKState) {              // depending on which button was pushed, we perform an action
        Serial.print(state_button);
    case CLICK_IDLE :
      if (button < 50 && state_button && (millis() - tmp > 50)) {
        tmp = millis();
        nextState = CLICK_INCREASE;
      }
      if (button > 450 && button < 650 && state_button && (millis() - tmp > 50)) {
        tmp = millis();
        nextState = CLICK_DECREASE;
      }
      break;

    case CLICK_INCREASE :
      nextState = CLICK_IDLE;
      break;

    case CLICK_DECREASE :
      nextState = CLICK_IDLE;
      break;
  }
  CLICKState = nextState;
}

void CLICK_output() {
  switch (CLICKState) {

    case CLICK_IDLE:
      break;

    case CLICK_INCREASE :
      Speed -= 100;
      if (Speed < 1000){
        Speed = 1000;
      }
      break;

    case CLICK_DECREASE :
      Speed += 100;
      if (Speed > 15000){
        Speed = 15000;
      }
      break;
  }
}

