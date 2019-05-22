package models;


import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class EdgeArrow extends Path {
        private static final double defaultArrowHeadSize = 7;
        private double startX, startY, endX, endY;
        
        public double getStartX() {
			return startX;
		}

		public void setStartX(double startX) {
			this.startX = startX;
		}

		public double getStartY() {
			return startY;
		}

		public void setStartY(double startY) {
			this.startY = startY;
		}

		public double getEndX() {
			return endX;
		}

		public void setEndX(double endX) {
			this.endX = endX;
		}

		public double getEndY() {
			return endY;
		}

		public void setEndY(double endY) {
			this.endY = endY;
		}

		public EdgeArrow(double startX, double startY, double endX, double endY, double arrowHeadSize){
            super();
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;

            strokeProperty().bind(fillProperty());
            setFill(Color.BLACK);

            //Line
            getElements().add(new MoveTo(startX, startY));
            getElements().add(new LineTo(endX, endY));

            //ArrowHead
            double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);
            //point1
            double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
            double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;
            //point2
            double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
            double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;

            getElements().add(new LineTo(x1, y1));
            getElements().add(new LineTo(x2, y2));
            getElements().add(new LineTo(endX, endY));
        }

        public EdgeArrow(double startX, double startY, double endX, double endY){
            this(startX, startY, endX, endY, defaultArrowHeadSize);
        }

}