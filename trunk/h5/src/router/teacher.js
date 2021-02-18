import TeacherLayout from '../pages/Teacher/TeacherLayout';
import CurriculumList from '../pages/Teacher/Curriculum/CurriculumList'
import LearningAnalysisList from '../pages/Teacher/LearningAnalysis/LearningAnalysisList'
import StudentList from '../pages/Teacher/LearningAnalysis/StudentList'
import StudentContrail from '../pages/Teacher/LearningAnalysis/StudentContrail'
import PersonalCenter from '../pages/Teacher/PersonalCenter'
import EvaluationResults from '../pages/Teacher/LearningAnalysis/EvaluationResults'
import LiveStream from '../pages/Teacher/Curriculum/LiveStream'
import RecordStream from '../pages/Teacher/Curriculum/RecordStream'
/**
 * 教师角色对应路由
 */
const commonData = [{
  path: 'Curriculum',
  name: 'TeacherCurriculum',
  meta: { title: '课表' },
  component: CurriculumList
}, {
  path: 'LearningAnalysis',
  name: 'LearningAnalysis',
  meta: { title: '学情监控' },
  component: LearningAnalysisList
}, {
  path: 'StudentList/:SubjectId',
  name: 'StudentList',
  meta: { title: '学生列表' },
  component: StudentList,
}, {
  path: 'StudentContrail/:StudentId',
  name: 'StudentContrail',
  meta: { title: '学生轨迹' },
  component: StudentContrail,
}, {
  path: 'QuestionResult',  //query:{ExamSetId,StudentId}
  name: 'QuestionResult',
  meta: { title: '测评报告' },
  component: EvaluationResults
}, {
  path: 'PersonalCenter',
  name: 'TeacherPersonalCenter',
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

export const offlineTeacher = [{
  path: '/Offline/Teacher',
  name: 'OfflineTeacher',
  meta: { title: '教师' },
  component: TeacherLayout,
  children: commonData
}];

export const onlineTeacher = [{
  path: '/Online/Teacher',
  name: 'OnlineTeacher',
  meta: { title: '教师' },
  component: TeacherLayout,
  children: commonData
}];
