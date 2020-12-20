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

package me.micrusa.amaztimer.utils.button.selector;

import android.content.Context;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import me.micrusa.amaztimer.R;
import me.micrusa.amaztimer.utils.devices.AmazfitUtils;

public class ButtonSelector extends Selector {
    private Button[] buttons;

    public ButtonSelector(Button[] buttons, Context context){
        super(context);
        this.buttons = buttons;
    }

    public void clickButton(){
        Button btn = buttons[selectedButton];
        btn.performClick();
    }

    public void refreshSelectedButton(){
        //Set default background for previous selected button
        for (Button button : buttons) {
            button.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_button));
        }

        //Make sure button is not under zero or over buttons length
        if(selectedButton < 0){
            selectedButton = buttons.length - 1;
        } else if(selectedButton >= buttons.length){
            selectedButton = 0;
        }

        //Set selected background for new selected button
        buttons[selectedButton].setBackground(ContextCompat.getDrawable(context, R.drawable.selected_button));
    }
}
