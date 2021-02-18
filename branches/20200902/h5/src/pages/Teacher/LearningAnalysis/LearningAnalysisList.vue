<template>
  <div class="contentWrap" :style="{ minHeight: windowHeight + 'px' }">
    <div class="tabs">
      <div
        class="tabItem"
        :class="{ selected: tabIndex === 0 }"
        v-on:click="ontabClick(0)"
      >
        课堂数据
      </div>
      <div
        class="tabItem"
        :class="{ selected: tabIndex === 1 }"
        v-on:click="ontabClick(1)"
      >
        人数统计
      </div>
    </div>
    <div v-if="tabIndex == 0">
      <div class="classDataList">
        <div
          class="item"
          v-for="(i, index) in teacherSubjectList"
          :key="index"
          v-on:click="gotoStudentList(i.SubjectId)"
        >
          <div class="wrap">
            <div class="time">{{ i.BeginTime | filterDateTime }}</div>
            <div class="className">{{ i.Name }}</div>
            <div class="personCount">
              {{ i.ClassAttendance }}<span class="personLetter">人</span>
            </div>
            <div class="praise">好评度：{{ i.Praise }}</div>
          </div>
        </div>
      </div>
      <div
        class="getMoreBtn"
        v-if="count > teacherSubjectList.length && !loading"
        v-on:click="getMore"
      >
        加载更多
      </div>
      <div class="getMoreBtn" v-if="loading">
        加载中
        <inline-loading></inline-loading>
      </div>
    </div>
    <div v-else>
      <div class="lableTitle">
        <select v-model="statisticType">
          <option value="1">上课人数</option>
          <option value="2" v-if="$store.state.CurUserSystem == 1"
            >出勤率</option
          >
        </select>
      </div>
      <div class="peopleQuery">
        <div
          class="item"
          :class="{ selected: days === 7 }"
          v-on:click="onQuerySwitch(7)"
        >
          近7日
        </div>
        <div
          class="item"
          :class="{ selected: days === 30 }"
          v-on:click="onQuerySwitch(30)"
        >
          近30日
        </div>
      </div>
      <v-chart
        class="peopleChartWrap"
        :data="attendList"
        v-show="statisticType == 1"
        :width="chartWidth"
      >
        <v-scale x field="BeginTime" :tick-count="4" />
        <v-scale y field="ClassAttendance" :min="0" />
        <v-bar :colors="['#36cbcb']" />
        <v-tooltip show-crosshairs show-x-value />
      </v-chart>
      <v-chart
        class="peopleChartWrap"
        :data="attendList"
        v-show="statisticType == 2"
        :width="chartWidth"
      >
        <v-scale x field="BeginTime" :tick-count="4" />
        <v-scale y field="AttendanceRate" :min="0" :formatter="formatter" />
        <v-bar :colors="['#36cbcb']" />
        <v-tooltip show-crosshairs show-x-value />
      </v-chart>
      <div v-if="schoolList.length > 0">
        <div class="schDescTitle">
          <span class="desc">学生学校占比</span>
        </div>
        <v-chart
          class="schChartWrap"
          prevent-render
          @on-render="renderChart"
          :width="chartWidth"
        ></v-chart>
      </div>
    </div>
  </div>
</template>

<script>
import {
  Tab,
  TabItem,
  Card,
  PopupPicker,
  Checker,
  CheckerItem,
  VChart,
  VLine,
  VArea,
  VBar,
  VPie,
  VPoint,
  VScale,
  VAxis,
  VTooltip,
  VLegend,
  VGuide,
  InlineLoading,
  dateFormat
} from "vux";
import {
  getHotSubjectList,
  getSubjectListStatistics,
  getStudentSchoolShare
} from "../../../services/getData";

export default {
  name: "LearningAnalysisList",
  components: {
    Tab,
    TabItem,
    Card,
    PopupPicker,
    Checker,
    CheckerItem,
    VChart,
    VLine,
    VArea,
    VBar,
    VPie,
    VPoint,
    VScale,
    VAxis,
    VTooltip,
    VLegend,
    VGuide,
    InlineLoading,
    dateFormat
  },
  data() {
    return {
      formatter: function(val) {
        return val.toFixed(0) + "%";
      },
      teacherSubjectList: [],
      tabIndex: 0,
      tabList: ["课堂数据", "人数统计"],
      statisticType: 1,
      days: 7,
      attendList: [{ BeginTime: "", Value: "" }],
      schoolList: [],
      htmlOptions: {
        position: ["50%", "45%"],
        html: `
          <div style="width: 250px;height: 40px;text-align: center;">
            <div style="font-size: 16px">人数</div>
            <div style="font-size: 24px">133.08 亿</div>
          </div>`
      },
      legendOptions: {
        position: "right",
        itemFormatter(val) {
          return val + "  " + map[val];
        }
      },
      studentCount: 0,
      pageIndex: 1,
      count: 0,
      loading: false,
      windowHeight: window.innerHeight - 32 * 1.5625,
      chartWidth: window.innerWidth - 32 * 1.25,
    };
  },
  created() {
    this.getHotSubjectList();
  },
  filters: {
    filterDateTime: function(date) {
      return dateFormat(date, "YYYY/MM/DD HH:mm");
    }
  },
  methods: {
    async getHotSubjectList() {
      var _this = this;
      let param = {
        TypeEnum: "1",
        PageSize: "20", //页码
        PageIndex: _this.pageIndex, //页索引 从1开始
        UserRole: "2"
      };
      _this.loading = true;
      await getHotSubjectList(param).then(res => {
        _this.loading = false;
        if (res != null) {
          let data = res.Details;
          data.map(function(value, index, array) {
            _this.teacherSubjectList.push(value);
          });
          _this.count = res.Count;
        }
      });
    },
    onQuerySwitch(value) {
      this.days = value;
      this.getSubjectListStatistics();
    },
    async getSubjectListStatistics() {
      var _this = this;
      let param = {
        Days: _this.days,
        UserRole: 2 //1学生，2老师，3管理员，4家长
      };
      await getSubjectListStatistics(param).then(res => {
        if (res != null) {
          _this.attendList = res.SubjectList;
        }
      });
    },
    async getStudentSchoolShare() {
      var _this = this;
      let param = {
        TypeEnum: 1,
        PageSize: 5
      };
      await getStudentSchoolShare(param).then(res => {
        if (res != null) {
          _this.schoolList = res.PieData;
          _this.studentCount = res.ValueCount;
        }
      });
    },
    ontabClick(itemId) {
      let _this = this;
      _this.tabIndex = itemId;
      if (itemId == 0) {
        _this.pageIndex = 1;
        _this.teacherSubjectList = [];
        _this.getHotSubjectList();
      } else {
        _this.days = 7;
        _this.statisticType = 1;
        _this.getSubjectListStatistics();
        _this.getStudentSchoolShare();
      }
    },
    renderChart({ chart }) {
      var _this = this;
      var data = _this.schoolList;
      var map = {};
      data.map(obj => {
        map[obj.Name] = obj;
      });
      chart.source(data);
      chart.axis(false);
      chart.tooltip(false);
      chart.coord("polar", {
        transposed: true,
        innerRadius: 0.7
      });
      chart.guide().html({
        position: ["50%", "50%"],
        html:
          '<div style="text-align:center;"><div style="width:80px;">人数</div><div>' +
          _this.studentCount +
          "</div></div>",
        style: {
          fontSize: 24
        }
      });
      chart.legend({
        position: "right",
        itemFormatter(val) {
          let temp = "",
            percent = "";
          if (val.length > 7) {
            temp = val.substring(0, 7) + "...";
          } else {
            temp = val;
          }
          percent = parseInt(
            (map[val].Value / _this.studentCount) * 100
          ).toString();
          percent = percent == "NaN" ? "0" : percent;
          return temp + "  " + percent + "%(" + map[val].Value + "人)";
        }
      });
      chart
        .interval()
        .position("*Value")
        .color("Name", [
          "#3ba0ff",
          "#36cbcb",
          "#4dcb73",
          "#fad337",
          "#f2637b",
          "#975fe4"
        ])
        .adjust("stack");
      chart.render();
    },
    gotoStudentList(id) {
      this.$router.push(
        `/${this.$store.state.BasePath}/Teacher/StudentList/${id}`
      );
    },
    getMore() {
      this.pageIndex++;
      this.getHotSubjectList();
    }
  }
};
</script>

<style lang="less">
@import "~vux/src/styles/reset.less";
html,
body {
  height: 100%;
  width: 100%;
  overflow-x: hidden;
}
#app {
  height: 100%;
}

.contentWrap {
  padding: 0 40px 40px;
  overflow-y: auto;
  background: url("~/static/images/openclass/bg.jpg") no-repeat;
  background-size: 100% 100%;
}

.tabs {
  background: none;
  display: flex;
  align-items: center;
  color: #ececec;
  font-size: 36px;
  .tabItem {
    flex: 1;
    text-align: center;
    line-height: 84px;
    position: relative;
    z-index: 0;
    cursor: pointer;
    &.selected {
      color: #fff;
      &::after {
        content: "";
        display: block;
        width: 68px;
        height: 16px;
        background-color: #fff;
        border-radius: 9px;
        position: absolute;
        bottom: 0;
        left: 50%;
        margin-left: -34px;
        transition: all 0.3s cubic-bezier(0.78, 0.14, 0.15, 0.86);
      }
    }
  }
}

.classDataList {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  .item {
    width: 48%;
    height: 268px;
    background: #fff;
    margin-top: 22px;
    border-radius: 8px;
    .wrap {
      padding: 12px 20px;
      line-height: 1;
      .time {
        font-size: 24px;
        color: #999;
      }
      .className {
        margin-top: 12px;
        font-size: 28px;
        color: #333;
        font-weight: bold;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
        line-height: normal;
        height: 75px;
      }
      .personCount {
        margin-top: 12px;
        font-size: 63px;
        color: #ff5252;
        .personLetter {
          font-size: 39px;
        }
      }
      .praise {
        margin-top: 20px;
        font-size: 26px;
        color: #4d4d4d;
      }
    }
  }
}

.getMoreBtn {
  font-size: 30px;
  color: #fff;
  margin-top: 30px;
  padding-bottom: 30px;
  text-align: center;
}

.desc {
  font-size: 36px;
  color: #fff;
}

.lableTitle select{
  font-size: 36px;
  color: #fff;
  border: none;
  background: transparent;
  option {
    color: #666;
  }
}

.schDescTitle {
  margin-top: 20px;
}

.peopleQuery {
  background: none;
  display: flex;
  align-items: center;
  color: #ececec;
  font-size: 36px;
  .item {
    text-align: center;
    line-height: 84px;
    position: relative;
    z-index: 0;
    cursor: pointer;
    &:first-child {
      margin-right: 40px;
    }
    &.selected {
      color: #fff;
      &::after {
        content: "";
        display: block;
        width: 68px;
        height: 16px;
        background-color: #fff;
        border-radius: 9px;
        position: absolute;
        bottom: 0;
        left: 50%;
        margin-left: -34px;
        transition: all 0.3s cubic-bezier(0.78, 0.14, 0.15, 0.86);
      }
    }
  }
}

.peopleChartWrap {
  margin-top: 20px;
  border-radius: 10px;
}

.schChartWrap {
  margin-top: 20px;
  border-radius: 10px;
}

.trangle {
  width: 20px;
  height: 16px;
  margin-left: 10px;
}
</style>
