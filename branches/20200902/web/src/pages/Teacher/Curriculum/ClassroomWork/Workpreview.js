import React, { Component } from 'react';
import TopicList from '../../../../components/TopicList';
import { connect } from 'dva';
@connect(({ curriculumModel }) => ({
  curriculumModel
}))
class Workpreview extends Component {
  constructor(props) {
    super(props);
    this.state = {
      results:{}
    }
  }
  componentDidMount(){
    const { dispatch ,location} = this.props;
    dispatch({
      type: "curriculumModel/GetExamSetPreview",
      payload: ({
        "ExamSetId": location.query.ExamSetId
      })
    }).then(result => {
      this.setState({
        results: result
      })
    })
  }

  render() {
      return (
      <TopicList
         topicListData={this.state.results}
        showType='readOnly'
          currentIndex={this.props.location.query.currentIndex}
      />
    );
  }
}
export default Workpreview;