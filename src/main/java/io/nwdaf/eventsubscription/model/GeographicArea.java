package io.nwdaf.eventsubscription.model;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
/**
* Geographic area specified by different shape.
*/
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Point.class, name = "string"),
  @JsonSubTypes.Type(value = Point.class, name = "Point"),
  @JsonSubTypes.Type(value = PointUncertaintyCircle.class, name = "PointUncertaintyCircle"),
  @JsonSubTypes.Type(value = PointUncertaintyEllipse.class, name = "PointUncertaintyEllipse"),
  @JsonSubTypes.Type(value = Polygon.class, name = "Polygon"),
  @JsonSubTypes.Type(value = PointAltitude.class, name = "PointAltitude"),
  @JsonSubTypes.Type(value = PointAltitudeUncertainty.class, name = "PointAltitudeUncertainty"),
  @JsonSubTypes.Type(value = EllipsoidArc.class, name = "EllipsoidArc")
})
public interface GeographicArea {

}
