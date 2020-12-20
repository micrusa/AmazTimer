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

import me.micrusa.amaztimer.utils.button.ButtonEvent;
import me.micrusa.amaztimer.utils.button.ButtonInterface;
import me.micrusa.amaztimer.utils.button.ButtonListener;
import me.micrusa.amaztimer.utils.devices.AmazfitUtils;

public abstract class Selector implements ButtonInterface {
    protected Context context;
    protected int selectedButton = 0;
    protected ButtonListener buttonListener;

    public Selector(Context context){
        if(!AmazfitUtils.isAmazfit()) return;
        buttonListener = new ButtonListener();
        this.context = context;
    }

    public abstract void refreshSelectedButton();
    public abstract void clickButton();

    public void startListening(){
        if(!AmazfitUtils.isAmazfit()) return;
        buttonListener.start(context, this);
        refreshSelectedButton();
    }

    public void stopListening(){
        if(!AmazfitUtils.isAmazfit()) return;
        buttonListener.stop();
    }

    @Override
    public void onKeyEvent(ButtonEvent e) {
        switch(e.getKey()){
            case ButtonEvent.KEY_UP:
                selectedButton--;
                refreshSelectedButton();
                break;
            case ButtonEvent.KEY_DOWN:
                selectedButton++;
                refreshSelectedButton();
                break;
            case ButtonEvent.KEY_SELECT:
                clickButton();
                break;
            default:
                break;
        }
    }
}
