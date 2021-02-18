import React, { Component, Fragment } from 'react';
import { Chart, Axis, Tooltip, Geom } from 'bizcharts';
import ReactEcharts from 'echarts-for-react';

class ColumnDiagram extends Component {
  render() {
    const { data, title, x, y } = this.props;
    const xy = `${x}*${y}`;
    const tooltip = [
      xy,
      (x2, y2) => ({
        name: x2,
        value: y == 'AttendanceRate' ? y2 + '%' : y2
      })
    ];

    return (
      <Fragment>
        {title && <h4 style={{ marginBottom: 20, marginLeft: 10 }}> {title} </h4>}
        {
          <Chart height={400} data={data} forceFit>
            <Axis name={x} />
            <Axis name={y} />
            <Tooltip showTitle={false} crosshairs={false} />
            <Geom type="line" position={xy} size={2} tooltip={tooltip} />
            <Geom type="point" position={xy} size={5} shape={'circle'} tooltip={tooltip} />
          </Chart>
        }
      </Fragment>
    );
  }
}
export default ColumnDiagram;
