package me.micrusa.amaztimer.utils.sensors.repsCounter.utils;

import java.util.ArrayList;

/**
 *
 * @author 2sloth
 * filter routine from "The scientist and engineer's guide to DSP" Chapter 20
 * filterOrder can be any even number between 2 & 20


 * cutoffFreq must be smaller than half the samplerate
 * filterType: 0=lowPass   1=highPass
 * ripplePercent is amount of ripple in Chebyshev filter (0-29) (0=butterworth)
 */
public class Filtering {
    public static double[] filterSignal(ArrayList<Float> signal, double sampleRate , double cutoffFreq, double filterOrder, int filterType, double ripplePercent) {
        double[][] recursionCoefficients =   new double[22][2];
        // Generate double array for ease of coding
        double[] unfilteredSignal =   new double[signal.size()];
        for (int i=0; i<signal.size(); i++) {
            unfilteredSignal[i] =   signal.get(i);
        }

        double cutoffFraction   =   cutoffFreq/sampleRate;  // convert cut-off frequency to fraction of sample rate
        System.out.println("Filtering: cutoffFraction: " + cutoffFraction);
        //ButterworthFilter(0.4,6,ButterworthFilter.Type highPass);
        double[] coeffA =   new double[22]; //a coeffs
        double[] coeffB =   new double[22]; //b coeffs
        double[] tA =   new double[22];
        double[] tB =   new double[22];

        coeffA[2]   =   1;
        coeffB[2]   =   1;

        // calling subroutine
        for (int i=1; i<filterOrder/2; i++) {
            double[] filterParameters   =   MakeFilterParameters(cutoffFraction, filterType, ripplePercent, filterOrder, i);

            for (int j=0; j<coeffA.length; j++){
                tA[j]   =   coeffA[j];
                tB[j]   =   coeffB[j];
            }
            for (int j=2; j<coeffA.length; j++){
                coeffA[j]   =   filterParameters[0]*tA[j]+filterParameters[1]*tA[j-1]+filterParameters[2]*tA[j-2];
                coeffB[j]   =   tB[j]-filterParameters[3]*tB[j-1]-filterParameters[4]*tB[j-2];
            }
        }
        coeffB[2]   =   0;
        for (int i=0; i<20; i++){
            coeffA[i]   =   coeffA[i+2];
            coeffB[i]   =   -coeffB[i+2];
        }

        // adjusting coeffA and coeffB for high/low pass filter
        double sA   =   0;
        double sB   =   0;
        for (int i=0; i<20; i++){
            if (filterType==0) sA   =   sA+coeffA[i];
            if (filterType==0) sB   =   sB+coeffB[i];
            if (filterType==1) sA   =   sA+coeffA[i]*Math.pow(-1,i);
            if (filterType==1) sB   =   sB+coeffA[i]*Math.pow(-1,i);
        }

        // applying gain
        double gain =   sA/(1-sB);
        for (int i=0; i<20; i++){
            coeffA[i]   =   coeffA[i]/gain;
        }
        for (int i=0; i<22; i++){
            recursionCoefficients[i][0] =   coeffA[i];
            recursionCoefficients[i][1] =   coeffB[i];
        }
        double[] filteredSignal =   new double[signal.size()];
        double filterSampleA    =   0;
        double filterSampleB    =   0;

        // loop for applying recursive filter
        for (int i= (int) Math.round(filterOrder); i<signal.size(); i++){
            for(int j=0; j<filterOrder+1; j++) {
                filterSampleA    =   filterSampleA+coeffA[j]*unfilteredSignal[i-j];
            }
            for(int j=1; j<filterOrder+1; j++) {
                filterSampleB    =   filterSampleB+coeffB[j]*filteredSignal[i-j];
            }
            filteredSignal[i]   =   filterSampleA+filterSampleB;
            filterSampleA   =   0;
            filterSampleB   =   0;
        }


        return filteredSignal;

    }
    /*  pi=3.14...
        cutoffFreq=fraction of samplerate, default 0.4  FC
        filterType: 0=LowPass   1=HighPass              LH
        rippleP=ripple procent 0-29                     PR
        iterateOver=1 to poles/2                        P%
    */
    // subroutine called from "filterSignal" method
    public static double[] MakeFilterParameters(double cutoffFraction, int filterType, double rippleP, double numberOfPoles, int iteration) {
        double rp   =   -Math.cos(Math.PI/(numberOfPoles*2)+(iteration-1)*(Math.PI/numberOfPoles));
        double ip   =   Math.sin(Math.PI/(numberOfPoles*2)+(iteration-1)*Math.PI/numberOfPoles);
        System.out.println("MakeFilterParameters: ripplP:");
        System.out.println("cutoffFraction  filterType  rippleP  numberOfPoles  iteration");
        System.out.println(cutoffFraction + "   " + filterType + "   " + rippleP + "   " + numberOfPoles + "   " + iteration);
        if (rippleP != 0){
            double es   =   Math.sqrt(Math.pow(100/(100-rippleP),2)-1);
//            double vx1  =   1/numberOfPoles;
//            double vx2  =   1/Math.pow(es,2)+1;
//            double vx3  =   (1/es)+Math.sqrt(vx2);
//            System.out.println("VX's: ");
//            System.out.println(vx1 + "   " + vx2 + "   " + vx3);
//            double vx   =   vx1*Math.log(vx3);
            double vx   =   (1/numberOfPoles)*Math.log((1/es)+Math.sqrt((1/Math.pow(es,2))+1));
            double kx   =   (1/numberOfPoles)*Math.log((1/es)+Math.sqrt((1/Math.pow(es,2))-1));
            kx  =   (Math.exp(kx)+Math.exp(-kx))/2;
            rp  =   rp*((Math.exp(vx)-Math.exp(-vx))/2)/kx;
            ip  =   ip*((Math.exp(vx)+Math.exp(-vx))/2)/kx;
            System.out.println("MakeFilterParameters (rippleP!=0):");
            System.out.println("es  vx  kx  rp  ip");
            System.out.println(es + "   " + vx*100 + "   " + kx + "   " + rp + "   " + ip);
        }

        double t    =   2*Math.tan(0.5);
        double w    =   2*Math.PI*cutoffFraction;
        double m    =   Math.pow(rp, 2)+Math.pow(ip,2);
        double d    =   4-4*rp*t+m*Math.pow(t,2);
        double x0   =   Math.pow(t,2)/d;
        double x1   =   2*Math.pow(t,2)/d;
        double x2   =   Math.pow(t,2)/d;
        double y1   =   (8-2*m*Math.pow(t,2))/d;
        double y2   =   (-4-4*rp*t-m*Math.pow(t,2))/d;
        double k    =   0;
        if (filterType==1) {
            k =   -Math.cos(w/2+0.5)/Math.cos(w/2-0.5);
        }
        if (filterType==0) {
            k =   -Math.sin(0.5-w/2)/Math.sin(w/2+0.5);
        }
        d   =   1+y1*k-y2*Math.pow(k,2);
        double[] filterParameters   =   new double[5];
        filterParameters[0] =   (x0-x1*k+x2*Math.pow(k,2))/d;           //a0
        filterParameters[1] =   (-2*x0*k+x1+x1*Math.pow(k,2)-2*x2*k)/d; //a1
        filterParameters[2] =   (x0*Math.pow(k,2)-x1*k+x2)/d;           //a2
        filterParameters[3] =   (2*k+y1+y1*Math.pow(k,2)-2*y2*k)/d;     //b1
        filterParameters[4] =   (-(Math.pow(k,2))-y1*k+y2)/d;           //b2
        if (filterType==1) {
            filterParameters[1] =   -filterParameters[1];
            filterParameters[3] =   -filterParameters[3];
        }
//        for (double number: filterParameters){
//            System.out.println("MakeFilterParameters: " + number);
//        }


        return filterParameters;
    }


}
