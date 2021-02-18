import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Button, Affix } from 'antd';
import styles from './index.less';

/**
 * 数值键盘组件
 * @param {数字键盘长度} numLength 
 * @param {点击事件} onClick 
 * @param {当前数字} currentNum 
 */
export default class NumberKeyboard extends Component{
    render(){
        const {numLength=0, currentNum=1, onChange, onSubmit} = this.props;        
        return (
            <Affix offsetTop={80}>
                <div className={styles.root}>
                    <div className={styles.title}>答题卡</div>
                    <div className={styles.operationArea}>
                        <ul className={`${styles.keyboard} f-clear`}>
                            {
                                [...Array(numLength)].map((o,i)=>{                            
                                    i = i+1;
                                    return (
                                        <li 
                                            key={i} 
                                            className={`${i == currentNum ? styles.liActive : ''}`}
                                            onClick={() => {onChange && onChange(i)}}
                                        >
                                            {i}
                                        </li>
                                    )
                                })
                            }                    
                        </ul>
                        <Button onClick={()=> {onSubmit && onSubmit()}} className={styles.btn} block>提交</Button>                
                    </div>
                </div>
            </Affix>
        );        
    }
};

NumberKeyboard.propTypes = {
    numLength: PropTypes.number,    //数值长度
    currentNum: PropTypes.number,   //当前选中数值
    onChange: PropTypes.func,       //选中数值键盘事件
    onSubmit: PropTypes.func,       //提交事件
}