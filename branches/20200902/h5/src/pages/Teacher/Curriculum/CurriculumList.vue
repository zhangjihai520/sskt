<template>
  <div class="curriculum" :style="{ minHeight: windowHeight + 'px' }">
    <div class="content">
      <div class="tabs">
        <div
          class="tabItem"
          :class="{ selected: dateType === 1 }"
          v-on:click="changeType(1)"
        >
          全部课程
        </div>
        <div
          class="tabItem"
          :class="{ selected: dateType === 0 }"
          v-on:click="changeType(0)"
        >
          我的课程
        </div>
      </div>
      <search
        class="serachWrap"
        :auto-fixed="false"
        @on-focus="onSearchFocus"
        @on-cancel="onSearchCancel"
        @on-change="onSearchChange"
        v-model="key"
      ></search>
      <div v-if="searching">
        <card v-for="i in searchedList" :key="i.SubjectId" class="searchItem" >
          <div slot="content" class="card-demo-flex">
            <div style="flex:1 1 auto;min-width:calc(100% - 80px);">
              <p style="font-size:0.7em;color:rgb(22,135,244)">
                {{ i.BeginTime | getDateTime }}
              </p>
              <div class="card-content">{{ i.SubjectName }}</div>
              <p class="card-content">主讲教师：{{ i.TeacherName }}</p>
            </div>
            <div
              class="rightbut"
              :style="{ display: dateType == 0 ? 'none' : '' }"
            >
              <x-button
                mini
                type="primary"
                style="margin-top:20px; width:80px;"
                @click.native="signUp($event,i.SubjectId)"
                :data-id="i.SubjectId"
                >
                {{
                  btn(i.StatusFlag, i.RegistEndTime)
                }}</x-button
              >
            </div>
          </div>
        </card>
      </div>
      <template v-if="!searching">
        <inline-calendar
          class="datawrap"
          :class="{ myData: dateType === 0 }"
          :show-last-month="false"
          :show-next-month="false"
          :render-function="buildSlotFn"
          v-model="selectedDate"
          @on-select-single-date="renderCurriculumList"
          @on-view-change="changeMonth"
        ></inline-calendar>
        <div class="list" :class="{ myListData: dateType === 0 }">
          <template v-if="curriculumList.length > 0">
            <card
              v-for="i in curriculumList"
              :key="i.SubjectId"
              class="itemWrap"
            >
              <div slot="content">
                <div class="item">
                  <p class="time">
                    {{ i.BeginTime | getTime }}
                  </p>
                  <p class="card-content">{{ i.SubjectName }}</p>
                  <p class="card-content">主讲教师：{{ i.TeacherName }}</p>
                </div>
                <div
                  class="rightbtn"
                >
                <x-button
                  mini
                  type="primary"
                  @click.native="signUp($event,i.SubjectId)"
                  :data-id="i.SubjectId"
                  >{{
                    btn(i.StatusFlag, i.RegistEndTime)
                  }}</x-button>
                </div>
              </div>
            </card>
          </template>
          <div v-else class="noneContent">
            当天没有课程
          </div>
        </div>
      </template>
      <toast v-model="show" type="text" :time="800" is-show-mask>{{
        msg
      }}</toast>
    </div>
  </div>
</template>

<script>
import {
  InlineCalendar,
  Card,
  dateFormat,
  Tabbar,
  TabbarItem,
  ButtonTab,
  ButtonTabItem,
  Search,
  XButton,
  Toast
} from "vux";
import {
  curriculumList,
  filtedCurriculumList,
  filtedCurriculumListAll,
  registSubject
} from "../../../services/getData";
export default {
  name: "CurriculumList",
  components: {
    InlineCalendar,
    Card,
    Tabbar,
    TabbarItem,
    ButtonTab,
    ButtonTabItem,
    Search,
    XButton,
    Toast
  },
  data() {
    return {
      buildSlotFn: (line, index, data) => {
        var _this = this;
        if (_this.markedList.join(",").indexOf(data.formatedDate) > -1) {
          return '<div style="background-color:red;height:1px;width:20px;margin:auto;margin-top:-1px;"></div>';
        } else {
          return "";
        }
      },
      dateType: 1, //0=我的 1 = 全部
      dateList: [],
      markedList: [],
      curriculumList: [],
      selectedDate: dateFormat(new Date(), "YYYY-MM-DD"),
      currentViewMonth: "",
      searching: false,
      searchedList: [],
      key: "",
      msg: "",
      show: false,
      windowHeight: window.innerHeight - 50
    };
  },
  filters: {
    getTime(date) {
      return dateFormat(date, "HH:mm");
    },
    getDateTime(date) {
      return dateFormat(date, "MM月DD日 EE HH:mm");
    }
  },
  created() {
    this.currentViewMonth = dateFormat(new Date(), "YYYY-MM");
    this.getCurriculumList(this.currentViewMonth);
  },
  methods: {
    async getCurriculumList(selectedMonth) {
      var _this = this;
      let param = {
        SelectedMonth: selectedMonth,
        TypeEnum: _this.dateType,
        SelectedDate: _this.selectedDate
      };
      await curriculumList(param).then(res => {
        if (res != null) {
          _this.markedList = [];
          for (let i in res.DateList) {
            _this.markedList.push(
              dateFormat(res.DateList[i].BeginDate, "YYYY-MM-DD")
            );
          }
          _this.dateList = res.DateList;
          _this.renderCurriculumList(_this.selectedDate);
        }
      });
    },
    async filtedCurriculumList() {
      var _this = this;
      let param = {
        Key: _this.key.trim()
      };
      if (_this.dateType == 0) {
        //我的-搜索
        await filtedCurriculumList(param).then(res => {
          if (res != null) {
            _this.searchedList = res.SubjectList;
          }
        });
      } else {
        //全部-搜索
        await filtedCurriculumListAll(param).then(res => {
          if (res != null) {
            _this.searchedList = res.SubjectList;
          }
        });
      }
    },
    renderCurriculumList(selectedDate) {
      var _this = this;
      _this.curriculumList = [];
      for (let i in _this.dateList) {
        if (
          dateFormat(_this.dateList[i].BeginDate, "YYYY-MM-DD") == selectedDate
        ) {
          this.curriculumList = _this.dateList[i].SubjectList;
        }
      }
    },
    changeMonth(data, index) {
      if (index != 0) {
        this.currentViewMonth = data.year + "-" + data.month;
        this.getCurriculumList(this.currentViewMonth);
      }
    },
    onSearchFocus() {
      this.searching = true;
      this.searchedList = [];
    },
    onSearchCancel() {
      this.searching = false;
    },
    onSearchChange() {
      this.filtedCurriculumList();
    },
    changeType(value) {
      this.dateType = value;
      this.searching = false;
      this.key = "";
    },
    signUp(e,subjectId) {
      // 只有 报名 能点击
      if(e.target.innerText == "进入课堂")
      {
        const url = `/Online/Teacher/LiveStream?subjectId=${subjectId}`;
        this.$router.push(url)
      }else if(e.target.innerText == "回看")
      {
        const url = `/Online/Teacher/RecordStream?subjectId=${subjectId}`;
        this.$router.push(url)
      }else{
        return;
      }

      
    },
    // 按钮置灰状态
    btnState(StatusFlag) {
      if (StatusFlag == 5||StatusFlag == 6) {
        return false;
      } else
      {
        return true
      }
    },
    // 按钮文字状态
    btn(StatusFlag, RegistEndTime) {
      let name = "";
      if (StatusFlag == 2) {
        name = "待报名";
      }else if (StatusFlag == 3 && new Date(RegistEndTime) > new Date()) {
        name = '报名中';
      }else if (StatusFlag == 4) {
        name = '待上课';
      }else if (StatusFlag == 5) {
        name = '回看';
      }else if (StatusFlag == 6){
        name = '进入课堂';
      }else
      {
        name = '待上课';
      }
      return name;
    }
  },
  watch: {
    dateType: function() {
      this.curriculumList = [];
      this.dateList = [];
      this.markedList = [];
      this.getCurriculumList(this.currentViewMonth);
    }
  }
};
</script>

<style lang="less">
@import "~vux/src/styles/reset.less";
.curriculum {
  width: 100%;
  overflow-x: hidden;
  overflow-y: auto;
  background: url("~/static/images/openclass/bg.jpg") no-repeat;
  background-size: 100% 100%;
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

  .serachWrap {
    .weui-search-bar {
      background-color: transparent;
      &:before {
        content: none;
      }
      &:after {
        content: none;
      }
      form {
        border-radius: 10px;
      }
    }
  }

  .searchItem {
    border-radius: 10px;
    margin: 5px 20px;
    padding: 20px;
    .card-demo-flex {
      display: flex;
    }
  }

  .datawrap {
    margin: 0 auto;
    margin-top: 25px;
    width: calc(100% - 8rem);
    height: 100vh;
    box-shadow: 0px 20px #ffffff7;
    border-radius: 64px;
    height: 690px;
    padding-top: 40px;
    /deep/ .calendar-year > span {
      left: 2rem;
    }
    /deep/ .calendar-year > span.calendar-header-right-arrow {
      left: auto;
      right: 2rem;
    }
    /deep/ .calendar-month > span {
      left: 2rem;
    }
    /deep/ .calendar-month > span.calendar-header-right-arrow {
      left: auto;
      right: 2rem;
    }
    /deep/ .vux-prev-icon,
    /deep/ .vux-next-icon {
      border-color: #ff5a7b;
    }
    /deep/ .calendar-title {
      color: #ff5a7b;
    }
    /deep/&.inline-calendar table {
      margin-top: 1.5rem;
      padding: 0 1rem;
      table-layout: fixed;
      border-collapse: separate;
    }
    /deep/ .is-week-list-0,
    /deep/ .is-week-0 {
      color: #ff5a7b;
    }
    /deep/ .is-today {
      color: #3b77ff;
    }
    /deep/&.inline-calendar td.current > span.vux-calendar-each-date {
      background-color: #ffdf34;
    }
    /deep/&.inline-calendar td {
      font-size: 0.875rem;
    }

    &.myData {
      /deep/&.inline-calendar td.current > span.vux-calendar-each-date {
        background-color: #ffad97;
      }
    }
  }

  .list {
    padding: 30px 32px 32px;
    /deep/ .weui-panel {
      background: none;
      margin-top: 0;
    }
    /deep/ .weui-panel:before {
      content: none;
    }
    /deep/ .weui-panel:after {
      border-color: #94c5ff;
    }
    .itemWrap {
      .vux-card-content {
        display: flex;
        justify-content: space-between;
        background-color: #3f87fe;
        align-items: center;
      }
      .item {
        padding: 20px 0 20px 60px;
        color: #fff;
        &:before {
          display: block;
          content: "";
          height: 110px;
          width: 16px;
          background-color: #ffdf34;
          border-radius: 8px;
          position: absolute;
          left: 12px;
        }
        .time {
          font-size: 36px;
        }
        .card-content {
          font-size: 28px;
          text-overflow: ellipsis;
          overflow: hidden;
          white-space: nowrap;
        }
      }
      .rightbtn button {
        width: 218px;
        height: 72px;
        line-height: 72px;
        border-radius: 10px;
        background: #fff;
        font-size: 28px;
        color: #3693ff;
        &.weui-btn_disabled {
          border-color: transparent;
          background: #76b5ff;
          color: #fff;
          &:after {
            content: none;
          }
        }
      }
    }
    &.myListData .item:before {
      background-color: #ffad97;
    }
  }

  .noneContent {
    text-align: center;
    font-size: 32px;
    color: #fff;
    margin-top: 40px;
  }
}
</style>
