//----------------------------------------------------
//功能描述：
//使用模块：
//创建时间:
//------------------修改记录-------------------
//修改人      修改日期        修改目的
//----------------------------------------------------
import React from 'react'
import styles from './index.less';
import nullImg from '../../../public/images/common/null-data.png';

/**
 * 教材列表
 */
export default class NullData extends React.Component {
  render() {
    const {isShow, message}=this.props;
    return (
      <div className={styles.dataListIsNull + (isShow ? "" : " dn")}>
        <img src={nullImg} className={styles.nullImg}/><br/>
        <label>{message}</label>
      </div>
    )
  }
}
