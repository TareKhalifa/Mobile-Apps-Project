package com.example.project;

import java.util.HashMap;

import kotlin.Pair;

public class Logic {
    static int n = 0;
    static HashMap<Pair<Integer, Integer>, Float> result = new HashMap<Pair<Integer, Integer>, Float>();
    static int minIdx(float[] a) {
        int idx = 0;
        for (int i = 1; i < n; i++)
            if (a[i] < a[idx])
                idx = i;
        return idx;
    }

    static int maxIdx(float[] a) {
        int idx = 0;
        for (int i = 1; i < n; i++)
            if (a[i] > a[idx])
                idx = i;
        return idx;
    }
    static void calc(float[] debts) {

        int highest = maxIdx(debts);
        int lowest = minIdx(debts);
        if (debts[highest] < 1 && debts[lowest] < 1)
            return;
        float min = Math.min((debts[lowest] * -1), debts[highest]);
        debts[highest] -= min;
        debts[lowest] += min;
        result.put(new Pair<>(lowest,highest),min);
        calc(debts);
    }
    public static HashMap<Pair<Integer, Integer>, Float> start(float debts[]) {
        n = debts.length;
        calc(debts);
        return result;
    }
}
