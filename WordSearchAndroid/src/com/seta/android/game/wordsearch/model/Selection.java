package com.seta.android.game.wordsearch.model;

import android.graphics.Point;
import android.widget.TextView;

public  class Selection {
        public static boolean isValidPoint(Point point, int max) {
                return (point.x >= 0 && point.x < max && point.y >= 0 && point.y < max);
        }
        public static Point getDeltas(Point pointStart, Point pointEnd) {
                if (pointEnd == null || pointStart == null) {
                        return null;
                }
                Point delta = new Point();
                delta.x = pointEnd.x-pointStart.x;
                delta.y = pointEnd.y-pointStart.y;
                if (Math.abs(delta.x) != Math.abs(delta.y) && delta.x != 0 && delta.y != 0) {
                        return null;
                }
                if (delta.x != 0) {
                        delta.x /= Math.abs(delta.x);
                }
                if (delta.y != 0) {
                        delta.y /= Math.abs(delta.y);
                }
                return delta;
        }
        /**
         * 
         * @param one a point in the grid
         * @param two another point in the grid
         * @return the number of iterations required to use the getDeltas to travel from point one to point two
         */
        public static int getLength(Point one, Point two) {
                return new Double(Math.sqrt(Math.pow(two.x-one.x, 2)+Math.pow(two.y-one.y, 2))).intValue();
        }
        private TextView start = null;
        private TextView end = null;
        public TextView getEnd() {
                return end;
        }
        public TextView getStart() {
                return start;
        }
        public boolean hasBegun() {
                return (end != null && start != null);
        }
        public void reset() {
                start = null;
                end = null;
        }
        public void setEnd(TextView end) {
                this.end = end;
        }
        public void setStart(TextView start) {
                this.start = start;
                this.end = start;
        }
}