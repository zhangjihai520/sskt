import React from 'react';
import styles from './index.less';

/**
 * 条件搜索栏
 */
var ConditionBar = function({ className="", ...props }) {
    return (
        <div className={`${styles.bar} ${className}`} {...props}>
            {props.children}
        </div>
    );
};

/**
 * 条件搜索子项
 */
var ConditionItem = function({ title, className="", ...props }) {
    return (
        <div className={`${styles.barItem} ${className}`} {...props}>
            <label>{title}</label>
            {props.children}
        </div>
    );
};

export default ConditionBar;
ConditionBar.ConditionItem = ConditionItem;
