import React, { Component } from 'react';
import { connect } from 'dva';
import { Icon, Button, Tag, Layout, Input } from 'antd';
import MyIcon from 'components/MyIcon';
import Loading from '../../../components/Loading/index';
import styles from './index.less';
import ListPagination from 'components/ListPagination';
import broadcastImg from 'assets/common/img_teacher_minclass.png';
import classImg from 'assets/common/img_teacher_class.png';
import Header from '../Header/index.js';
import PasswordLogin from '../../Access/PasswordLogin';
import { getTokenByCookie } from 'utils/storage';
import { filterCourse } from 'utils/utils';
import BGImg from 'assets/common/bg_img.jpg';
import BGContent from 'assets/common/bg_content.png';

const { CheckableTag } = Tag;
const { Content } = Layout;

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
      loginVisible: false,
      keyWord: '',
      courseList: _.cloneDeep(props.CourseList)
    };
  }

  componentDidMount() {
    const { dispatch } = this.props;
    dispatch({ type: 'commonModel/getCourseGroupTypeListNoLogin' }).then(result => {
      this.setState({
        courseList: _.cloneDeep(this.props.CourseList)
      });
    });
    this.queryResult();
  }

  handleGradeChange = item => {
    let courseList = _.cloneDeep(this.props.CourseList);
    if (item.Id != 0) {
      _.remove(courseList, function(course) {
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
    this.setState({ pageIndex: 1 }, () => {
      this.queryResult();
    });
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
    if (getTokenByCookie()) {
      const { StudentBasePath } = this.props;
      window.location = `${StudentBasePath}/MinLesson/MinLessonVideo?WeiKeId=${WeiKeId}`;
    } else {
      this.setState({ loginVisible: true });
    }
  };

  changeModalStatus = status => {
    this.setState({ loginVisible: status });
  };

  render() {
    const { GradeList } = this.props;
    const { checkGrade, checkCourse, pageIndex, pageSize, count, data, loginVisible, keyWord, courseList } = this.state;

    filterCourse(courseList);

    const GradeQuery = () => {
      let result;
      if (GradeList) {
        result = GradeList.map(item => {
          return (
            <CheckableTag
              key={item.Id}
              checked={item.Id === checkGrade}
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
          checked={checkGrade === 0}
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
              checked={item.Id === checkCourse}
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
          checked={checkCourse === 0}
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
      <Layout>
        <Header {...this.props} visible={loginVisible} changeModalStatus={this.changeModalStatus} />
        <PasswordLogin visible={loginVisible} changeModalStatus={this.changeModalStatus} />
        <div className={styles.bg}>
          <div className={styles.bgImg} />
          <div className={styles.bgContent} />
        </div>
        <Content className={styles.studentContentLoyout + ' ' + 'gd'}>
          <div className={styles.MinLesson}>
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
                  pageSizeOptions={['12', '24', '48','96']}
                  onChange={this.handleChangePage}
                />
              </div>
              <Loading isLoading={this.state.isLoading} loadingText={this.state.loadingText} />
            </div>
          </div>
        </Content>
      </Layout>
    );
  }
}

function stateToProp(state) {
  return {
    GradeList: state.commonModel.GradeList,
    CourseList: state.commonModel.CourseList,
    StudentBasePath: state.commonModel.StudentBasePath
  };
}

export default connect(stateToProp)(MinLesson);
