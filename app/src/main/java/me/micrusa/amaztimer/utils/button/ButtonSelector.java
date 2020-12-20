/*
 * MIT License
 *
 * Copyright (c) 2020 Miguel Cruces
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.micrusa.amaztimer.utils.button;

import android.content.Context;
import android.os.Handler;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.utils.devices.AmazfitUtils;

public class ButtonSelector implements ButtonInterface {
    private Context context;
    private Handler handler;
    private Button[] buttons;
    private int selectedButton = -1;
    private ButtonListener buttonListener;


    public ButtonSelector(Button[] buttons, Context context){
        if(!AmazfitUtils.isAmazfit()) return;
        handler = new Handler(context.getMainLooper());
        buttonListener = new ButtonListener();
        this.buttons = buttons;
        this.context = context;
        selectButton(0);
    }

    @Override
    public void onKeyEvent(ButtonEvent e) {
        switch(e.getKey()){
            case ButtonEvent.KEY_UP:
                handler.post(() -> selectButton(selectedButton - 1));
                break;
            case ButtonEvent.KEY_DOWN:
                handler.post(() -> selectButton(selectedButton + 1));
                break;
            case ButtonEvent.KEY_SELECT:
                handler.post(this::clickButton);
                break;
            default:
                break;
        }
    }

    private void clickButton(){
        Button btn = buttons[selectedButton];
        btn.performClick();
    }

    private void selectButton(int button){
        //Set default background for previous selected button
        if(selectedButton >= 0)
            buttons[selectedButton].setBackground(ContextCompat.getDrawable(context, R.drawable.circle_button));

        //Make sure button is not under zero or over buttons length
        if(button < 0){
            button = buttons.length - 1;
        } else if(button >= buttons.length){
            button = 0;
        }

        //Set selected background for new selected button
        selectedButton = button;
        Button btn = buttons[button];
        btn.setBackground(ContextCompat.getDrawable(context, R.drawable.selected_button));
    }

    public void startListening(){
        if(!AmazfitUtils.isAmazfit()) return;
        buttonListener.start(context, this);
        selectButton(0);
    }

    public void stopListening(){
        if(!AmazfitUtils.isAmazfit()) return;
        buttonListener.stop();
    }
}
