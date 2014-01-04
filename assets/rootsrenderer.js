function RootsRenderer() {
  this.points = new Array();
  this.target = $("#rootsrenderer");
}

/**
 * @brief Clear all the points in the plot. 
 */
RootsRenderer.prototype.clear = function () {
  this.points = new Array();
  this.redraw();
}

RootsRenderer.prototype.addPoint = function (point) {
  this.points.push (point);
}

RootsRenderer.prototype.redraw = function () {
  $.plot(this.target, [{ 
    data: this.points, 
    points: { 
      show: true,
    }, 
    colors: [ "#f22", "#22f" ]
  }]);
}
