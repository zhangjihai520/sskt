import React, { Component } from 'react';
import { connect } from 'dva';
import { routerRedux } from 'dva/router';
import { Icon, Button, Tag, Input } from 'antd';
import MyIcon from 'components/MyIcon';
import Loading from '../../../components/Loading/index';
import styles from './index.less';
import ListPagination from 'components/ListPagination';
import broadcastImg from 'assets/common/img_teacher_minclass.png';
import classImg from 'assets/common/img_teacher_class.png';
import { filterCourse } from 'utils/utils';

const { CheckableTag } = Tag;

class MinLesson extends Component {
  constructor(props) {
    super(props);
    this.state = {
      pageSize: 12,
      pageIndex: 1,
      checkGrade: 0,
      checkCourse: 0,
      count: 0,
      data: undefined,
      isLoading: true,
      loadingText: '正在加载中',
      keyWord: '',
      courseList: _.cloneDeep(this.props.CourseList)
    };

    if (props.UserBaseInfo && props.UserBaseInfo.GradeId) {
      this.state.checkGrade = props.UserBaseInfo.GradeId * 1;
    }
  }
  
  componentDidMount() {
    const { GradeList } = this.props;
    const { checkGrade } = this.state;
    let result = _.find(GradeList, function (item) {
      return item.Id == checkGrade;
    });
    if (!result) {
      result = { Id: 0 };
    }
    this.handleGradeChange(result);
  }

  handleGradeChange = item => {
    let courseList = _.cloneDeep(this.props.CourseList);
    if(courseList.length==0){
      setTimeout(() => {
        location.reload()
      }, 1000);
    }
    if (item.Id != 0) {
      _.remove(courseList, function (course) {
        return course.CourseTypeId != item.CourseTypeId;
      });
    }

    this.setState({ courseList: courseList, checkGrade: item.Id, checkCourse: 0, pageIndex: 1 }, () => {
      this.queryResult();
    });
  };

  handleCourseChange = id => {
    this.setState({ checkCourse: id, isLoading: true }, () => {
      this.queryResult();
    });
  };

  handleChangePage = state => {
    this.setState(
      {
        pageIndex: state.PageIndex,
        pageSize: state.PageSize,
        isLoading: true
      },
      () => {
        this.queryResult();
      }
    );
  };

  handleKeyWordChange = e => {
    this.setState({
      keyWord: e.target.value
        .replace(/(\\*)(\/*)(\:*)(\**)(\?*)("*)(<*)(>*)(\|*)(\#*)(\`*)(&*)(\'*)/g, '')
        .substring(0, 50)
    });
  };

  handleSearch = () => {
    this.queryResult();
  };

  queryResult = () => {
    const { checkGrade, checkCourse, pageSize, pageIndex, checkType, keyWord } = this.state;

    let param = {
      PageSize: pageSize,
      PageIndex: pageIndex,
      CourseId: checkCourse,
      GradeId: checkGrade,
      StatusFlag: 2,
      KeyWord: keyWord
    };

    this.props
      .dispatch({
        type: 'smallnModel/GetWeiKeList',
        payload: param
      })
      .then(result => {
        this.setState({
          count: result.TotalCount,
          data: result.WeiKeList,
          isLoading: false
        });
      });
  };

  handleVideoClick = WeiKeId => {
    const { StudentBasePath, dispatch } = this.props;
    dispatch(routerRedux.push(`${StudentBasePath}/MinLesson/MinLessonVideo?WeiKeId=${WeiKeId}`));
  };

  render() {
    const { GradeList } = this.props;
    const { checkGrade, checkCourse, pageIndex, pageSize, count, data, keyWord, courseList } = this.state;

    filterCourse(courseList);
    const GradeQuery = () => {
      let result;
      if (GradeList) {
        result = GradeList.map(item => {
          return (
            <CheckableTag
              key={item.Id}
              checked={item.Id == checkGrade}
              onChange={() => {
                this.handleGradeChange(item);
              }}
            >
              {item.Name}
            </CheckableTag>
          );
        });
      }

      return [
        <CheckableTag
          key={0}
          checked={checkGrade == 0}
          onChange={() => {
            this.handleGradeChange({ Id: 0 });
          }}
        >
          全部
        </CheckableTag>,
        result
      ];
    };

    const CourseQuery = () => {
      let result;
      if (courseList) {
        result = courseList.map(item => {
          return (
            <CheckableTag
              key={item.Id}
              checked={item.Id == checkCourse}
              onChange={() => {
                this.handleCourseChange(item.Id);
              }}
            >
              {item.LongName}
            </CheckableTag>
          );
        });
      }

      return [
        <CheckableTag
          key={0}
          checked={checkCourse == 0}
          onChange={() => {
            this.handleCourseChange(0);
          }}
        >
          全部
        </CheckableTag>,
        result
      ];
    };

    const dataContent = () => {
      if (data) {
        if (data.length == 0) {
          return <div className={styles.nullPage}>暂无数据</div>;
        }
        return data.map((item, index) => {
          return (
            <div key={item.WeiKeId} className={styles.box}>
              <div
                className={styles.video}
                onClick={() => {
                  this.handleVideoClick(item.WeiKeId);
                }}
              >
                <img src={item.OverViewUrl ? item.OverViewUrl : item.BookVersionName === "复习专题课" ? classImg : broadcastImg} width="240" height="140" />
                <MyIcon className={styles.icon} type="video" />
              </div>
              <p className={styles.title}>{item.WeiKeName}</p>
              <p className={styles.source}>
                由<span className={styles.author}>{item.CreateUserName}</span>主讲
              </p>
            </div>
          );
        });
      }
    };

    return (
      <div>
        <div className={styles.bg}>
          <div className={styles.bgImg} />
          <div className={styles.bgContent} />
        </div>
        <div className={styles.MinLesson + ' ' + 'gd'}>
          <div className={styles.queryWrap}>
            <Input
              className={styles.keyword}
              placeholder="请输入课程名称"
              value={keyWord}
              onChange={this.handleKeyWordChange}
            />
            <Button className={styles.btn} type="primary" onClick={this.handleSearch}>
              搜索
            </Button>
          </div>
          <div className={styles.headSelect}>
            <div>
              <span className={styles.leftlabel}>年级：</span>
              <div className={styles.selectionbox}>{GradeQuery()}</div>
            </div>
            <div>
              <span className={styles.leftlabel}>学科：</span>
              <div className={styles.selectionbox}>{CourseQuery()}</div>
            </div>
          </div>
          <div className={styles.container}>
            <div className={styles.titlePanel}>
              <span className={styles.lableDesc}>课程列表</span>
            </div>
            <div className={styles.videlbox}>{dataContent()}</div>
            <div className={styles.pag}>
              <ListPagination
                total={count}
                customShowTotal={(total, range) => {
                  return <span className="tl">显示{range[0]}-{range[1]}，共{total}条记录</span>;
                }}
                pageIndex={pageIndex}
                defaultPageSize={12}
                pageSizeOptions={['12', '25', '50']}
                onChange={this.handleChangePage}
              />
            </div>
            <Loading isLoading={this.state.isLoading} loadingText={this.state.loadingText} />
          </div>
        </div>
      </div>
    );
  }
}

function stateToProp(state) {
  return {
    GradeList: state.commonModel.GradeList,
    CourseList: state.commonModel.CourseList,
    StudentBasePath: state.commonModel.StudentBasePath,
    UserBaseInfo: state.commonModel.UserBaseInfo
  };
}

export default connect(stateToProp)(MinLesson);
