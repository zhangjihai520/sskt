import React, { Component } from 'react';
import { connect } from 'dva';
import PublicClassComponent from "components/PublicClass";

class PublicClass extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (<PublicClassComponent dispatch={this.props.dispatch} />);
  }
}

function stateToProp(state) {
  return {
  };
}

export default connect(stateToProp)(PublicClass);
