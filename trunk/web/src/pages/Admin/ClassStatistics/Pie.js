import React,{Component} from 'react';
import {
  Chart,
  Geom,
  Axis,
  Tooltip,
  Coord,
  Label,
  Legend,
  Guide,
} from "bizcharts";
import { Divider } from 'antd';
import styles from './Pie.less';
import { DataView } from "@antv/data-set";
import classNames from 'classnames';

class Pie extends Component{
    state = {
      legendData: [],
    }
    /**
     * 获取图例列表
     */
    getLegendData = () => {
      if (!this.chart) return;
      const geom = this.chart.getAllGeoms()[0]; // 获取所有的图形
      if (!geom) return;
      const items = geom.get('dataArray') || []; // 获取图形对应的

      const legendData = items.map(item => {
        const origin = item[0]._origin;
        origin.color = item[0].color;
        return origin;
      });

      this.setState({
        legendData,
      });
    };
    /**
     * 获取 chart 实例的回调
     */
    getG2Instance = chart => {
      this.chart = chart;
      requestAnimationFrame(() => {
        this.getLegendData();
      });
    };
    render(){
      const { Html } = Guide;
      const {data,subTitle,total} = this.props;
      const dv = new DataView();
      const {legendData} = this.state;

      if(!data) return false;

      dv.source(data).transform({
        type: "percent",
        field: "Value",
        dimension: "Name",
        as: "percent"
      });
      const cols = {
        x: {
          type: 'Name',
        },
        y: {
          type: 'Value',
          min: 0,
        },
        percent: {
          formatter: val => {
            val = val * 100 + "%";
            return val;
          }
        }
      };

      const tooltipFormat = [
        'Name*percent',
        (item, p) => ({
          name: item,
          value: `${(p * 100).toFixed(2)}%`,
        }),
      ];
    const html = `<div style="color:#8c8c8c;font-size:20px;text-align: center;">${subTitle}<br><span style="color:#262626;font-size:20px">${total}</span></div>`

      return (
        <div className={classNames(styles.legendBlock,styles.pie,styles.legendBlock)}>
          <div className={styles.chart}>
            <Chart
              height={400}
              data={dv}
              scale={cols}
              padding={[12, 0, 12, 0]}
              animate={true}
              forceFit
              onGetG2Instance={this.getG2Instance}
              >
                <Coord type={"theta"} radius={0.75} innerRadius={0.6} />
                <Guide>
                  <Html
                    position={["50%", "50%"]}
                    html={html}
                    alignX="middle"
                    alignY="middle"
                  />
                </Guide>
                <Tooltip  showTitle={false}  />
                <Geom
                  style={{ lineWidth:4, stroke: '#fff' }}
                  type="intervalStack"
                  position="percent"
                  tooltip={tooltipFormat}
                  color="Name"
                />
            </Chart>
          </div>
          <ul className={styles.legend}>
            {legendData.map((item, i) => (
              <li key={i}>
                <span
                  className={styles.dot}
                  style={{ backgroundColor: item.color}}
                />
                <span className={styles.legendTitle} title={item.Name}>{item.Name}</span>
                <Divider type="vertical" />
                <span className={styles.percent}>
                  {`${(Number.isNaN(item.percent) ? 0 : item.percent * 100).toFixed(2)}%`}
                </span>
                <span className={styles.value}>{item.Value}</span>
              </li>
            ))}
          </ul>
        </div>
      );
    }
}

export default Pie;

