package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    // TODO entirely your job (except onCircle)

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {


        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for( Shape s: g.getShapes()){
            Location box = s.accept(this);
            Rectangle rect = (Rectangle) box.getShape();

            int right = box.getX() + rect.getWidth();
            int bottom = box.getY() + rect.getHeight();

            minX = Math.min(minX, box.getX());
            minY = Math.min(minY, box.getY());
            maxX = Math.max(maxX, right);
            maxY = Math.max(maxY, bottom);
        }

        return new Location(minX, minY,
                new Rectangle(maxX - minX, maxY - minY));
    }


    @Override
    public Location onLocation(final Location l) {
    final Location inner = l.getShape().accept(this);
        return new Location(
                l.getX() + inner.getX(),
                l.getY() + inner.getY(),
                inner.getShape()
        );


    }

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0, 0, new Rectangle(r.getWidth(), r.getHeight()));
    }




    @Override
    public Location onStrokeColor(final StrokeColor c) {

        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {

        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (Point point : s.getPoints()) {
            minX = Math.min(minX, point.getX());
            minY = Math.min(minY, point.getY());
            maxX = Math.max(maxX, point.getX());
            maxY = Math.max(maxY, point.getY());
        }

        return new Location(
                minX,
                minY,
                new Rectangle(maxX - minX, maxY - minY)
        );
    }
}
