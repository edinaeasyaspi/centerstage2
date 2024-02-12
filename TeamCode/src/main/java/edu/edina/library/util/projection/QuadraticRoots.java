package edu.edina.library.util.projection;

public class QuadraticRoots {
    public static double[] realRoots(double a, double b, double c) throws RootNotFound {
        double d = b * b - 4 * a * c;
        if (d < 0)
            throw new RootNotFound();

        double s = Math.sqrt(d);
        return new double[]{
                (-b + s) / (2 * a),
                (-b - s) / (2 * a)
        };
    }
}
