import React, { Component } from 'react';
import { Spin } from 'antd';

/**
 * loading组件
 */
class Loading extends Component {
    /**
     * 渲染DOM
     */
    render() {
        return (
            <Spin tip={this.props.loadingText || '正在加载中'} spinning={this.props.isLoading} />
        )
    }
}

export default Loading;