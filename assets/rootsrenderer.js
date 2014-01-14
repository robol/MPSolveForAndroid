var RootsRendererState = new Object();

RootsRendererState.PLOT = 1; 
RootsRendererState.APPROXIMATION_LIST = 2;

function RootsRenderer() {
  this.points = new Array();
  this.target = $("#rootsrenderer");

  // Possibile values for this are PLOT and 
  // APPROXIMATION_LIST. 
  this.state = RootsRendererState.PLOT;
  
  this.target.resize(this.redraw);
}

/**
 * @brief Clear all the points in the plot. 
 */
RootsRenderer.prototype.clear = function () {
  this.points = new Array();
  this.target.html("");
}

RootsRenderer.prototype.addPoint = function (point) {
  this.points.push (point);
}

RootsRenderer.prototype.switchView = function () {
  this.target.html("");

  switch (this.state) {
    case RootsRendererState.PLOT:
      this.state = RootsRendererState.APPROXIMATION_LIST;
      this.writeRoots();
      break;

    case RootsRendererState.APPROXIMATION_LIST:
      this.state = RootsRendererState.PLOT;
      this.redraw();
      break;

    default:
      console.log ("Unhandled state in RootsRenderer");
      this.state = RootsRendererState.PLOT;
      break;
  }
}

RootsRenderer.prototype.writeRoots = function () {
  list_html = "<h2>List of approximations</h2><ul>";

  for (i = 0; i < this.points.length; i++) {
    var sep = "+"; 
    if (this.points[i][1] < 0)
      sep = "";

    list_html += "  <li>" + this.points[i][0] + sep + 
       this.points[i][1]  + "i </li>"; 
  }

  list_html += "</ul>"; 

  this.target.html(list_html);
}

RootsRenderer.prototype.redraw = function () {
  if (this.target.height() > 0 && this.target.width() > 0) {
      $.plot(this.target, [{ 
        data: this.points, 
        points: { 
          show: true,
        }, 
        colors: [ "#f22", "#22f" ]
      }]);
  }
}
