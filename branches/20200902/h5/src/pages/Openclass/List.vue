<template>
  <div class="page">
    <div class="container">
      <div class="tabs">
        <div class="tabItem"
          v-for="item in tabs"
          :key="item.Id"
          :class="{ selected: gradeId===item.Id }"
          @click="onTabClick(item.Id)"
          >
            {{item.name}}
        </div>
      </div>
      <div class="dateSwiper">
        <div class="arr" @click="onClickLeft"><img src="../../../static/images/openclass/left_arr.png" /></div>
        <div class="swiperWrapper" ref="swiper">
          <div class="swiper" :style="transformStyle">
            <div
              class="swiperItem"
              :class="{ selected: currentDate === item.date }"
              v-for="item in dates"
              :key="item.date"
              :style="itemStyle"
              @click="onSelectDate(item.date)">
              <p>{{item.week}}</p>
              <p class="date">{{item.date}}</p>
            </div>
          </div>
        </div>
        <div class="arr" @click="onClickRight"><img src="../../../static/images/openclass/right_arr.png" /></div>
      </div>
      <div class="list" v-if="list && list.length > 0">
        <section v-for="item in list" :key="item.Date">
          <div class="title">{{item.Date}}</div>
          <div
            class="cardItem"
            v-for="subject in item.GroupClassInfoList"
            :key="subject.SubjectId"
            :class="{
              'now': subject.StatusFlag === 6,
              'enable': subject.StatusFlag <= 4,
              'do': subject.StatusFlag ===5 && subject.FileDownUrl,
              'init': subject.IsZzzy === true
            }">
            <div class="cardHeader">{{subject.BeginTime}} {{subject.CourseName}}</div>
            <div class="cardContent">
              <div class="courseName">{{subject.SubjectName}}</div>
              <template v-if="subject.IsZzzy === true">
                <button class="button">无直播</button>
              </template>
              <template v-else-if="subject.FileDownUrl && subject.StatusFlag > 4">
                <button
                  class="button"
                  @click="onRedirectToUrl"
                >立刻练习</button>
                <x-icon class="xicon" type="ios-help-outline" size="30"  @click="showPdf"></x-icon>
              </template>
              <template v-else>
                <button
                  class="button"
                  v-if="subject.StatusFlag===5"
                  @click="onRedirectToDetail(subject.SubjectId)">点击回看</button>
                <button
                  class="button"
                  v-else-if="subject.StatusFlag===6"
                  @click="onRedirectToDetail(subject.SubjectId)">正在直播</button>
                <button class="button" v-else>暂未开始</button>
              </template>
            </div>
          </div>
        </section>
      </div>
      <div v-else style="text-align:center;line-height:200px;color:#FFF;">暂无数据</div>
    </div>
  </div>
</template>

<script>
import { dateFormat, Card, Cell } from 'vux';
import { GetPublicBenefitSubjectList, CurrentTime } from "../../services/getData";
let lock = false; // 请求锁

export default {
  name: 'OpenClassIndex',
  components: {
    Card,
    Cell
  },
  data() {
    return {
      tabs: [{
        Id: 3,
        name: '初三年级中考复习'
      }, {
        Id: 6,
        name: '高三年级专题课'
      }],
      gradeId: 3,
      currentDate: '',
      UserRole: 1, // 1学生，2老师，3管理员，4家长
      dates: [], // 显示的日期列表
      list: [],
      dateWidth: 0,
      itemStyle: {
        width: 0
      },
      transformStyle: {
        transform: `translate3d(0, 0, 0)`,
        width: 0,
      },
      totalWidth: 0,
      transformLength: 0,
      transformMin: -900,
      transfromBase: 200,
    }
  },
  mounted() {
    const UserRole = localStorage.getItem('UserRole') || 1;
    this.UserRole = UserRole;
    this.getCurrentTime();
  },
  methods: {
    // 获取当前直播的时间
    async getCurrentTime(callback) {
      const res = await CurrentTime({
        InputDate: ''
      });
      this.dates = [];

      const today = dateFormat(res.CurrentDate, 'YYYY-MM-DD');
      const beginDate = dateFormat(res.MinDate, 'YYYY-MM-DD');
      const endDate = dateFormat(res.MaxDate, 'YYYY-MM-DD');
      const datesArray = [];
      this.currentDate = today;

      this.buildDateArray(beginDate, endDate, today, datesArray);

      // 计算宽度
      const width = this.$refs.swiper.clientWidth;
      const itemWith = width / 3;
      const totalWidth = datesArray.length * itemWith;
      let todayIndex = 0;

      datesArray.forEach((item, index) => {
        if (item.week === '今天') {
          todayIndex = index;
          return false;
        }
        if(index === datesArray.length-1)
        {
          this.currentDate = datesArray[0].date;
        }
      });
      const initTranslateX =  Math.floor(todayIndex / 3) * width // 计算出默认进来的translateX
      this.itemStyle.width = itemWith + 'px';
      this.transformStyle = {
        transform: `translate3d(-${initTranslateX}px, 0, 0)`,
        width: totalWidth + 'px'
      }
      this.transfromBase = width;
      this.transformMin = width - totalWidth;
      this.totalWidth = totalWidth;
      this.dates = datesArray;
      this.getLessonData()
    },
    async getLessonData() {
      const { UserRole, gradeId, currentDate } = this;
      if (lock) {
        return false;
      }
      this.$vux.loading.show({
        text: '加载中'
      })
      lock = true;
      const res = await GetPublicBenefitSubjectList({
        Date: currentDate, //日期
        GradeId: gradeId, //年级枚举值
        CourseId:0, //所属学科枚举值
        UserRole // 1学生，2老师，3管理员，4家长
      })
      lock = false;
      this.$vux.loading.hide();
      this.list = res.DateGroup
    },
    // 生成日期数组
    buildDateArray(date, endDate, today, arr) {
      const NOW = new Date(date);
      arr.push({
        week: today === date ? '今天' : '星期' + dateFormat(NOW, 'E'),
        date: dateFormat(NOW, 'YYYY-MM-DD')
      })
      if (dateFormat(NOW, 'YYYY-MM-DD') === endDate) {
        return false
      }
      const NextDate = NOW.setDate(NOW.getDate() + 1);
      this.buildDateArray(dateFormat(NextDate, 'YYYY-MM-DD'), endDate, today, arr);
    },
    // 选择年级
    onTabClick(id) {
      if (this.gradeId === id) {
        return false
      }
      this.gradeId = id;
      this.getCurrentTime();
      this.getLessonData();
    },
    onSelectDate(date) {
      if (this.currentDate === date) {
        return false;
      }
      this.currentDate = date;
      this.getLessonData();
    },
    // 跳转到详情页面
    onRedirectToDetail(subjectId) {
      const gradeId = this.gradeId
      const date = this.currentDate
      const url = `/OpenClass/Detail?subjectId=${subjectId}&date=${date}&gradeId=${gradeId}`;
      this.$router.push(url)
    },
    onRedirectToUrl() {
      window.location.href = 'http://app.motk.com/android.html?c=A01';
    },
    // 点击左移按钮
    onClickLeft() {
      const { transformLength, transfromBase } = this;
      let result = transformLength;
      if (transformLength + transfromBase <= 0) {
        result = transformLength + transfromBase;
      } else {
        result = 0;
      }
      this.transformLength = result;
      this.transformStyle = {
        transform: `translate3d(${result}px, 0, 0)`,
        width: this.totalWidth + 'px'
      }
    },
    // 点击右移按钮
    onClickRight() {
      const { transformLength, transfromBase, transformMin } = this;
      let result = transformLength;
      if (transformLength - transfromBase > transformMin) {
        result = transformLength - transfromBase;
      } else {
        result = transformMin;
      }
      this.transformLength = result;
      this.transformStyle = {
        transform: `translate3d(${result}px, 0, 0)`,
        width: this.totalWidth + 'px'
      }
    },
    showPdf() {
      window.location.href = 'http://sskt.motk.com/course/加入班级&获取课后作业操作指南.pdf'
    }
  }
}
</script>

<style lang="less" scoped>
@import "~vux/src/styles/reset.less";
.page {
  height: 100vh;
  width: 100%;
  overflow-x: hidden;
  overflow-y: auto;
  background: url('../../../static/images/openclass/bg.jpg') no-repeat;
  background-size: 100% 100%;
}
.container {
  padding: 0 25px 20px;
}
.tabs {
  background: none;
  display: flex;
  align-items: center;
  height: 108px;
  color: #ececec;
  font-size: 36px;

  .tabItem {
    flex: 1;
    text-align: center;
    line-height: 108px;
    position: relative;
    z-index: 0;
    cursor: pointer;

    &.selected {
      color: #fff;

      &::after {
        content: '';
        display: block;
        width: 54px;
        height: 10px;
        background-color: #fff;
        border-radius: 6px;
        position: absolute;
        bottom: 0;
        left: 50%;
        margin-left: -27px;
        transition: all 0.3s cubic-bezier(0.78, 0.14, 0.15, 0.86);
      }
    }
  }
}
.dateSwiper {
  overflow: hidden;
  padding: 15px 0 20px;
  display: flex;
  align-items: center;
  .swiperWrapper {
    flex: 1;
    height: 120px;
    overflow: hidden;
    position: relative;
    z-index: 1;
  }
  .swiper {
    color: #ececec;
    font-size: 17px;
    transition: transform 0.3s cubic-bezier(0.78, 0.14, 0.15, 0.86);

    .swiperItem {
      box-sizing: border-box;
      float: left;
      margin: 0;
      padding: 0;
      text-align: center;
      height: 100px;
      font-variant: tabular-nums;
      list-style: none;
      font-feature-settings: 'tnum';
      display: block;
      height: auto;
      padding: 0 6px;
      position: relative;
      z-index: 1;
      font-size: 34px;
      white-space: nowrap;
      cursor: default;
      opacity: 1;
      transition: all 0.3s cubic-bezier(0.78, 0.14, 0.15, 0.86);

      .date {
        font-size: 34px;
      }
      &.selected {
        color: #fff;

        &::after {
          content: '';
          display: block;
          width: 27px;
          height: 5px;
          background-color: #fff;
          border-radius: 3px;
          position: absolute;
          bottom: -10px;
          left: 50%;
          margin-left: -13.5px;
          transition: all 0.3s cubic-bezier(0.78, 0.14, 0.15, 0.86);
        }
      }
    }
  }
  .arr {
    padding: 0 10px;
    cursor: pointer;
    img {
      width: 18px;
      height: 32px;
    }
  }
}

.list {
  border-top: solid #97cdff 1px;
  padding-top: 30px;

  .title {
    margin-bottom: 10px;
    font-size: 36px;
    color: #fff
  }

  .cardItem {
    background: #fff;
    border-radius: 5px;
    margin-bottom: 10px;
    .cardHeader {
      padding: 32px;
      font-size: 30px;
      color: #666;
      border-bottom: solid 1px #ccc;
    }
    .cardContent {
      color: #333;
      padding: 32px;
      align-items: center;
      display: flex;
    }
    .courseName {
      flex: 1;
      font-size: 30px;
    }
    .xicon {
      margin-left: 10px;
    }
    .button {
      width: 192px;
      height: 68px;
      line-height: 68px;
      padding: 0;
      margin: 0;
      font-size: 28px;
      border-radius: 34px;
      border: solid 1px #4173f2;
      background: none;
      color: #4173f2;
    }
  }

  .now {
    .cardContent {
      color: #fc670b;
    }
    .button {
      border-color: #fc670b;
      color: #fc670b;
    }
    .xicon {
      fill: #fc670b
    }
  }

  .enable {
    .cardContent {
      color: #999;
    }
    .button {
      border-color: #999;
      color: #999;
    }
  }

  .do {
    .button {
      border-color: #333;
      color: #333;
    }
  }

  .init {
    .cardContent {
      color: #333;
    }
    .button {
      border-color: #333;
      color: #333;
    }
    .xicon {
      fill: #333
    }
  }
}
</style>
