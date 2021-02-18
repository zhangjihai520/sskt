import * as services from '../services/classScheduleServices';
import { routerRedux } from 'dva/router';
import { message } from 'antd';
import { downloadByForm } from 'utils/utils';
import { roleEnum } from '../config/enum';

const SubjectRoomsNull = [
	{
		SubjectRoomId: '',
		SubjectRoomName: '',
		SchoolName: '',
		HelpTeacherId: '',
		HelpTeacherName: '',
		MaxRegisterNumber: 0
	}
];

export default {
	namespace: 'classScheduleModel',
	state: {
		ClassInfoList: [],
		PageIndex: 1,
		PageSize: 25,
		Count: 0,
		InfoData: {
			SubjectRooms: SubjectRoomsNull
		},
		GradeId: undefined,
		StatusFlag: undefined,
		BeginTimeMin: undefined,
		BeginTimeMax: undefined,
		Key: undefined
	},

	reducers: {
		save(state, action) {
			return {
				...state,
				...action.payload
			};
		},
		updateClassInfoList(state, action) {
			return {
				...state,
				ClassInfoList: action.payload
			};
		},
		updateClassInfo(state, action) {
			return {
				...state,
				InfoData: action.payload
			};
		},
		updateSubjectRoom(state, action) {
			return {
				...state,
				InfoData: {
					...state.InfoData,
					SubjectRooms: action.payload
				}
			};
		},
	},

	effects: {
		/**
		 * 获取
		 */
		*fetch({ payload }, { call, put }) {
			let response = yield call(services.GetSubjectList, payload);

			//添加表格需要的key值
			response.ClassInfoList.map(item => {
				item.key = item.SubjectId;
			});
			response.PageIndex = payload.PageIndex;
			response.PageSize = payload.PageSize;
			response.GradeId = payload.GradeId;
			response.StatusFlag = payload.StatusFlag;
			response.BeginTimeMin = payload.BeginTimeMin;
			response.BeginTimeMax = payload.BeginTimeMax;
			response.Key = payload.Key;
			yield put({
				type: 'save',
				payload: response
			});
		},
		/**
		 * 清除state info
		 */
		*clearInfo({ payload }, { call, put }) {
			let InfoData = {
				SubjectRooms: SubjectRoomsNull
			};
			yield put({
				type: 'updateClassInfo',
				payload: InfoData
			});
		},
		//课表管理列表
		*GetSubjectList({ payload }, { call }) {
			return yield call(services.GetSubjectList, payload);
		},
		//下载课程数据 、导出课程数据
		*ExportSubjectList({ payload }) {
			downloadByForm('/api/Subject/ExportSubjectList', payload);
		},
		/**
		 * 教师首页获取当天课程通知
		 * @param {*} param0
		 * @param {*} param1
		 */
		*GetCurrentSubiject({ payload }, { call }) {
			return yield call(services.GetCurrentSubiject, payload);
		},
		/**
		 * 修改上架,下架，删除状态
		 */
		*update({ payload }, { call, put, select }) {
			const { StatusFlag, SubjectId } = payload;
			let response = yield call(services.UpdateSubjectStatus, { StatusFlag, SubjectId });

			if (response == 1) {
				//删除
				if (payload.StatusFlag == 0) {
					yield put({
						type: 'fetch',
						payload: {
							UserRole: roleEnum.admin,
							PageIndex: 1, //页码
							PageSize: yield select(state => state.classScheduleModel.PageSize)
						}
					});
				} else {
					// 上/下架
					const calssList = yield select(state =>
						state.classScheduleModel.ClassInfoList.map(item => {
							if (item.SubjectId === payload.SubjectId) {
								if (payload.StatusFlag == 1) {
									item.StatusFlag = 2; // 待報名
								} else {
									item.StatusFlag = 1; //待上架
								}
							}
							return item;
						})
					);

					yield put({
						type: 'updateClassInfoList',
						payload: calssList
					});
				}
			}
		},
		/**
		 * 保存课程
		 */
		*saveClass({ payload }, { call, put, select }) {
			let response = yield call(services.EditSubject, payload);
			let basePath = yield select(state => state.commonModel.AdminBasePath);
			let ListQuery = yield select(state => state.classScheduleModel.ListQuery);

			if (response == 1) {
				message.success('保存成功');
				yield put(routerRedux.push({ pathname: `${basePath}/ClassSchedule`, query: ListQuery }));
			} else {
				message.error('保存失败');
			}
		},
		/**
		 * 获取课程详情
		 */
		*getClassInfo({ payload }, { call, put }) {
			let response = yield call(services.GetSubjectInfo, payload);
			//ImagePath 编辑时需转换数据
			response.ImagePath = [
				{
					uid: '-1',
					url: response.ImagePath
				}
			];
			yield put({
				type: 'updateClassInfo',
				payload: response
			});
			return response;
		},
		*addClassRoom({ payload }, { call, put, select }) {
			const info = yield select(state => {
				return state.classScheduleModel.InfoData.SubjectRooms.concat(SubjectRoomsNull);
			});

			yield put({
				type: 'updateSubjectRoom',
				payload: info
			});
		},
		*getTeacher({ payload }, { call, put }) {
			return yield call(services.SearchTeacherInfo, payload);
		},
		*delClassInfo({ payload }, { call, put, select }) {
			const info = yield select(state => {
				state.classScheduleModel.InfoData.SubjectRooms.splice(payload.index, 1);
				return state.classScheduleModel.InfoData.SubjectRooms;
			});

			yield put({
				type: 'updateSubjectRoom',
				payload: info
			});
		},
		/**
		 * 获取助教列表
		 */
		*GetSubjectRoomList({ payload }, { call, put }) {
			return yield call(services.GetSubjectRoomList, payload);
		},
		/**
		 * 获取学生列表
		 */
		*GetGroupByStudentList({ payload }, { call, put }) {
			return yield call(services.GetGroupByStudentList, payload);
		},
		/**
		 * 学生分组
		 */
		*SaveGroupByStudentList({ payload }, { call, put }) {
			return yield call(services.SaveGroupByStudentList, payload);
		},
	},
	subscriptions: {}
};
