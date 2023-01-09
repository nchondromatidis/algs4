/**
 * Created by nikos on 8/3/2014.
 */
import java.util.Arrays;

public class Fast {
    public static void main(String[] args) {
        // read in the input
        String filename = args[0];
        //String filename = "collinear/input3.txt";
        In in = new In(filename);
        int N = in.readInt();
        if(N<4) return;
        Point points[] = new Point[N];
        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.show(0);
        //create the point objects
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
            points[i].draw();
        }
        //sort point in their natural order
        Arrays.sort(points);
        //create a copy of original array to maintain sorting order
        Point orderedPoints[] = new Point[N];
        for (int i = 0; i < N ; i++) {
            orderedPoints[i] = points[i];
        }
        //sort by slope each point in the original array
        int cpStart;
        int cpEnd;
        Fast fast = new Fast();
        for (Point point : points) {
            Arrays.sort(orderedPoints, point.SLOPE_ORDER);
            //find collinear points
            cpStart = 1;
            cpEnd = 0;
            double previousSlope = orderedPoints[0].slopeTo(orderedPoints[1]);
            for(int i=1;i<N;i++) {
                double currentSlope = orderedPoints[0].slopeTo(orderedPoints[i]);
                int doubleCompare = Double.compare(previousSlope, currentSlope);
                if((doubleCompare!=0) ) {
                    if((cpEnd-cpStart)>=2) fast.printSubsegment(orderedPoints, cpStart, cpEnd);
                    cpStart = i;
                    cpEnd = i;
                } else if (doubleCompare==0 && cpEnd==i-1) {
                    cpEnd = i;
                }
                previousSlope = currentSlope;
            }
            fast.printSubsegment(orderedPoints, cpStart, cpEnd);
        }
        StdDraw.show(0);
    }


    private void printSubsegment(Point[] orderedPoints, int cpStart, int cpEnd){
        boolean sortedCollinearFlag=true;
        //check if collinear points exist and are more than 3
        int collinearPoints = cpEnd-cpStart+1;
        if(collinearPoints >= 3) {
            //check if collinear points are sorted and max length
            sortedCollinearFlag = true;
            for(int i=cpStart;i<=cpEnd;i++) {
                if(orderedPoints[0].compareTo(orderedPoints[i])>0) sortedCollinearFlag=false;
            }
            if(sortedCollinearFlag==true){
                Arrays.sort(orderedPoints,cpStart,cpEnd+1);
                if(cpStart!=0) StdOut.print(orderedPoints[0]);
                for(int i=cpStart;i<=cpEnd;i++) {
                    StdOut.print(" -> " + orderedPoints[i]);
                }
                orderedPoints[0].drawTo(orderedPoints[cpEnd]);
                StdOut.println();
            }

        }
    }
}