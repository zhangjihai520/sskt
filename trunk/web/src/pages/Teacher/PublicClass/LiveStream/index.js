import React, { Component } from 'react';
import { connect } from 'dva';
import LiveStreamComponent from "components/PublicClass/LiveStream";

@connect(({}) => ({}))
export default class LiveStream extends Component {
	constructor(props) {
		super(props);
	}

	render() {
		return (<LiveStreamComponent dispatch={this.props.dispatch} />);
	}
}
