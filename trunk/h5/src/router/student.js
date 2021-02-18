import CurriculumList from '../pages/Student/Curriculum/CurriculumList'
import HomeworkList from '../pages/Student/Homework/HomeworkList'
import QuestionPractice from '../pages/Student/Homework/QuestionPractice'
import PersonalCenter from '../pages/Student/PersonalCenter/index'
import StudentLayout from '../pages/Student/StudentLayout'
import EvaluationResults from '../pages/Student/Homework/EvaluationResults'
import LiveStream from '../pages/Student/Curriculum/LiveStream'
import RecordStream from '../pages/Student/Curriculum/RecordStream'
const commonData = [{
  path: 'Curriculum',
  name: 'StudentCurriculum',
  meta: { title: '首页' },
  component: CurriculumList,
}, {
  path: 'Homework',
  name: 'StudentHomework',
  meta: { title: '作业练习' },
  component: HomeworkList,
}, {
  path: 'HomeworkDetail',  //query:{ExamSetId}
  name: 'StudentPractice',
  meta: { title: '做题' },
  component: QuestionPractice
}, {
  path: 'EvaluationResults',  //query:{ExamSetId}
  name: 'EvaluationResults',
  meta: { title: '测评报告' },
  component: EvaluationResults
}, {
  path: 'PersonalCenter',
  name: 'StudentPersonalCenter',
  meta: { title: '个人中心' },
  component: PersonalCenter
},{
  path: 'LiveStream',
  name: 'TeacherLiveStream',
  meta: { title: '课程直播'},
  component: LiveStream
},{
  path: 'RecordStream',
  name: 'TeacherRecordStream',
  meta: { title: '课程回放'},
  component: RecordStream
}]

export const offlineStudent = [{
  path: '/Offline/Student',
  name: 'OfflineStudent',
  meta: { title: '学生' },
  component: StudentLayout,
  children: commonData
}];

export const onlineStudent = [{
  path: '/Online/Student',
  name: 'OnlineStudent',
  meta: { title: '学生' },
  component: StudentLayout,
  children: commonData
}];
