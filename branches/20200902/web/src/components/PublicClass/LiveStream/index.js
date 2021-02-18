import React, { Component } from 'react';
import { Button, Modal, Input, message, Icon, Slider, Tabs } from 'antd';
import $ from 'jquery';
import style from './index.less';
import { connect } from 'dva';
import { getUrlQuery } from 'utils/utils';
import classnames from 'classnames';
import _ from 'lodash';
import moment from 'moment';
import defaultAvator from 'assets/common/AntiVirus/default_avator.png';
import imgExcel from 'assets/common/AntiVirus/EXCEL.png';
import imgPdf from 'assets/common/AntiVirus/PDF.png';
import imgPPT from 'assets/common/AntiVirus/PPT.png';
import imgWord from 'assets/common/AntiVirus/WORD.png';
import MotkIMG from 'assets/common/AntiVirus/motk_img.png';

const { TabPane } = Tabs;
const { TextArea } = Input;

@connect(({}) => ({}))
export default class LiveStream extends Component {
	constructor(props) {
		super(props);
		this.state = {
			subjectId: '',
			dateGroup: [],
			currentGrade: 3,
			live: true,
			fullscreen: false,
			list: [],
			message: ''
		};
		this.videoRef = React.createRef();
		this.videoAreaRef = React.createRef();
		this.wrap = React.createRef();
	}

	componentDidMount() {
		this.loop = true;
		this.active = true;
		this.getSource();
		// reset style
		$('.gd').css({ width: '100%' });

		this.videoRef.current.volume = 0.5;
	}

	componentWillUnmount() {
		this.loop = false;
		$('.gd').css({ width: '' });
	}

	getSource = () => {
		const SubjectId = getUrlQuery(window.location.href, 'SubjectId');
		const currentDate = getUrlQuery(window.location.href, 'currentDate');
		const currentGrade = getUrlQuery(window.location.href, 'currentGrade'); // 初三Id 3，高三Id 6
		const { dispatch } = this.props;

		this.setState({ subjectId: SubjectId, currentGrade: currentGrade }, () => {
			currentGrade == 3 && this.getMessage();
		});

		dispatch({
			type: 'commonModel/RecordLog',
			payload: {
				ObjectKey: SubjectId,
				RecordType: 0
			}
		});

		dispatch({
			type: 'stuModel/GetPublicBenefitSubjectList',
			payload: {
				Date: currentDate,
				GradeId: currentGrade,
				CourseId: 0,
				UserRole: 1,
				IsWeb: 1
			}
		}).then(result => {
			this.setState(
				{
					dateGroup: result.DateGroup
				},
				() => {
					this.videoPlay(SubjectId);
				}
			);
		});
	};

	getMessage = () => {
		const { subjectId } = this.state;
		const { dispatch } = this.props;

		dispatch({
			type: 'stuModel/Message',
			payload: {
				SubjectId: subjectId,
				TypeId: 3,
				PageSize: 20
			}
		}).then(result => {
			result.TalkMessageList.forEach(item => {
				if (item.TalkTypeId == 1) {
					item.Content = item.Content.replace(/(\r\n|\n|\r)/gm, '<br />');
				}
			});
			this.setState({ list: result.TalkMessageList }, () => {
				$('#wrap').scrollTop($('#wrap').prop('scrollHeight'));
				this.loopMessage();
			});
		});
	};

	loopMessage = event => {
		const { subjectId, list } = this.state;
		const { dispatch } = this.props;
		const self = this;
		const BoundaryId = list.length > 0 ? list[list.length - 1].TalkMessageId : '';

		Promise.race([
			dispatch({
				type: 'stuModel/Message',
				payload: {
					SubjectId: subjectId,
					BoundaryId: BoundaryId,
					TypeId: 2,
					PageSize: 20
				}
			}),
			new Promise(function(resolve, reject) {
				setTimeout(() => reject(new Error('request timeout')), 10000);
			})
		])
			.then(data => {
				if (!self.loop) {
					return;
				}
				if (data.TalkMessageList.length > 0) {
					data.TalkMessageList.forEach(item => {
						if (item.TalkTypeId == 1) {
							item.Content = item.Content.replace(/(\r\n|\n|\r)/gm, '<br />');
						}
					});
					if (self.active) {
						self.setState(
							{
								list: list.concat(data.TalkMessageList).slice(-500),
								alertMessage: false
							},
							() => {
								$('#wrap').scrollTop($('#wrap').prop('scrollHeight'));
								setTimeout(() => {
									self.loopMessage();
								}, 1500);
							}
						);
					} else {
						this.setState(
							{
								list: list.concat(data.TalkMessageList).slice(-500),
								alertMessage: true
							},
							() => {
								setTimeout(() => {
									self.loopMessage();
								}, 1500);
							}
						);
					}
				} else {
					setTimeout(() => {
						self.loopMessage();
					}, 1500);
				}
			})
			.catch(() => {
				setTimeout(() => {
					self.loopMessage();
				}, 1500);
			});
	};

	handleScroll = e => {
		if ($('#wrap').scrollTop() + $('#wrap').outerHeight() == $('#wrap').prop('scrollHeight')) {
			this.setState({ alertMessage: false });
			this.active = true;
		} else {
			this.active = false;
		}
	};

	handleFullScreen = () => {
		const ele = this.videoAreaRef.current;
		if (
			document.fullscreen ||
			document.mozFullScreen ||
			document.webkitIsFullScreen ||
			document.webkitFullScreen ||
			document.msFullScreen
		) {
			if (document.exitFullScreen) {
				document.exitFullScreen();
			} else if (document.mozCancelFullScreen) {
				document.mozCancelFullScreen();
			} else if (document.webkitExitFullscreen) {
				document.webkitExitFullscreen();
			} else if (document.msExitFullscreen) {
				document.msExitFullscreen();
			}

			this.setState({ fullscreen: false });
		} else {
			if (ele.requestFullscreen) {
				ele.requestFullscreen();
			} else if (ele.mozRequestFullScreen) {
				ele.mozRequestFullScreen();
			} else if (ele.webkitRequestFullscreen) {
				ele.webkitRequestFullscreen();
			} else if (ele.msRequestFullscreen) {
				ele.msRequestFullscreen();
			}
			this.setState({ fullscreen: true });
		}
	};

	handleBtnClick = (item, btnDisable) => {
		if (!btnDisable) {
			if (item.PopupUrl) {
				window.open(item.PopupUrl);
			} else if (item.FileDownUrl) {
				window.open(item.FileDownUrl);
			} else {
				this.setState(
					{
						subjectId: item.SubjectId,
						list: []
					},
					() => {
						this.videoPlay(item.SubjectId);
					}
				);
			}
		}
	};

	handleVolumeChange = value => {
		this.videoRef.current.volume = value / 100;
	};

	handleAnswerChange = e => {
		this.setState({ message: e.target.value });
	};

	handlePressEnter = e => {
		if (e.key === 'Enter' && e.shiftKey != true) {
			e.preventDefault();
			this.handleSubmitMessage();
		}
	};

	handleSubmitMessage = () => {
		const { subjectId, message } = this.state;
		const { dispatch } = this.props;
		const self = this;
		if (this.state.message.trim() == '') {
			return;
		}
		dispatch({
			type: 'stuModel/SendMessage',
			payload: {
				SubjectId: subjectId,
				MessageContent: message
			}
		}).then(result => {
			if (result == 1) {
				self.setState({ message: '' });
			}
		});
	};

	videoPlay = subjectId => {
		const { dateGroup } = this.state;
		const { dispatch } = this.props;
		let findData = undefined;
		_.find(dateGroup, item => {
			findData = _.find(item.GroupClassInfoList, lessonItem => {
				return subjectId == lessonItem.SubjectId;
			});
			return findData;
		});

		if (findData) {
			dispatch({
				type: 'stuModel/CurrentTime',
				payload: {
					InputDate: findData.EndTime,
					BeginTime: findData.BeginTime
				}
			}).then(result => {
				const video = this.videoRef.current;
				if (result.SpaceOfTime >= 0) {
					this.setState({ live: true }, () => {
						video.src = `${decodeURI(findData.PPTVideoUrl)}?t=${Math.random()}`;
						video.currentTime = result.SpaceOfTime;
						video.play();
					});
				} else {
					this.setState({ live: false }, () => {
						video.src = `${decodeURI(findData.PPTVideoUrl)}?t=${Math.random()}`;
						video.play();
					});
				}
			});
		}
	};

	render() {
		const { subjectId, dateGroup, currentGrade, live, fullscreen, list, message } = this.state;

		const messContent = item => {
			switch (item.TalkTypeId) {
				case 1:
					return (
						<div className={style.mess}>
							<div className={style.triangle}></div>
							<div className={style.normal} dangerouslySetInnerHTML={{ __html: item.Content }}></div>
						</div>
					);
					break;
				case 2:
					return (
						<div className={style.mess}>
							<div className={style.triangle}></div>
							<a href={item.AttachmentUrl} target="_blank">
								<img className={style.image} src={item.ThumbImageUrl} />
							</a>
						</div>
					);
					break;
				case 3:
					const AffixExtent = item.AttachmentUrl.substring(item.AttachmentUrl.lastIndexOf('.') + 1).toLowerCase();
					let affixImgSrc = '';
					if (AffixExtent.indexOf('ppt') > -1) {
						affixImgSrc = imgPPT;
					} else if (AffixExtent.indexOf('xls') > -1) {
						affixImgSrc = imgExcel;
					} else if (AffixExtent.indexOf('pdf') > -1) {
						affixImgSrc = imgPdf;
					} else if (AffixExtent.indexOf('doc') > -1) {
						affixImgSrc = imgWord;
					}
					return (
						<div className={style.mess}>
							<div className={style.triangle}></div>
							<div className={style.affix}>
								<img className={style.affixImg} src={affixImgSrc} />
								<div className={style.desc}>
									<div className={style.filename}>
										<a href={item.AttachmentUrl} target="_blank">
											{item.Content}
										</a>
									</div>
									<div className={style.filesize}>{item.FileSize}</div>
								</div>
							</div>
						</div>
					);
					break;
			}
		};

		const displayTypeCheck = () => {
			let findData = undefined;
			_.find(dateGroup, item => {
				findData = _.find(item.GroupClassInfoList, lessonItem => {
					return subjectId == lessonItem.SubjectId;
				});
				return findData;
			});

			if (findData && findData.StatusFlag == 6) {
				return true;
			} else {
				return false;
			}
		}

		return (
			<div className={style.contentArea}>
				<div className={style.videoArea} ref={this.videoAreaRef}>
					<video
						className={live ? classnames(style.videoEl, style.live) : style.videoEl}
						controls={!live}
						controlsList="nodownload"
						ref={this.videoRef}
						autoPlay={true}
					>
						对不起, 您的游览器不支持视频播放.
					</video>
					{live && (
						<div className={style.controlPanel}>
							<Icon type="sound" />
							<div className={style.sliderPanel}>
								<Slider defaultValue={50} onChange={this.handleVolumeChange} />
							</div>
							<Icon
								className={style.clickAble}
								type={fullscreen ? 'fullscreen-exit' : 'fullscreen'}
								onClick={this.handleFullScreen}
							/>
						</div>
					)}
				</div>
				<div className={style.operationArea}>
					<img className={style.motkImg} src={MotkIMG} />
					<Tabs defaultActiveKey="1" className={style.tabs}>
						<TabPane tab="课程列表" key="1" forceRender={true}>
							<div className={style.scrollArea}>
								{dateGroup &&
									dateGroup.map((item, index) => {
										return (
											<div className={style.lessonArea} key={`group${index}`}>
												<div className={style.title}>{item.Date}</div>
												{item.GroupClassInfoList &&
													item.GroupClassInfoList.map((lessonItem, lessonIndex) => {
														let curstyle = style.lessonItem,
															btnName = '暂未开课',
															btnDisable = true,
															acitve = '';

														if (subjectId === lessonItem.SubjectId) {
															acitve = style.active;
														}
														if (lessonItem.StatusFlag == 6) {
															curstyle = classnames(style.lessonItem, style.cur, acitve);
															btnName = '正在直播';
															if (lessonItem.FileDownUrl) {
																btnName = '立刻练习';
															}
															btnDisable = false;
														} else if (lessonItem.StatusFlag == 5) {
															curstyle = classnames(style.lessonItem, style.review, acitve);
															btnName = '点击回看';
															if (lessonItem.FileDownUrl) {
																btnName = '立刻练习';
															}
															btnDisable = false;
														}

														if (lessonItem.IsZzzy) {
															curstyle = classnames(style.lessonItem, style.init);
															btnName = '无直播';
															btnDisable = true;
														}

														return (
															<div className={curstyle} key={`lesson${lessonIndex}`}>
																<div className={style.courseName}>
																	<div>{lessonItem.CourseName}</div>
																	<div>{moment(lessonItem.BeginTime).format('H:mm')}</div>
																</div>
																<div className={style.name}>{lessonItem.SubjectName}</div>
																<div
																	className={style.btn}
																	onClick={() => {
																		this.handleBtnClick(lessonItem, btnDisable);
																	}}
																>
																	{btnName}
																</div>
																<div className={style.lineAngle} />
															</div>
														);
													})}
											</div>
										);
									})}
							</div>
						</TabPane>
						{currentGrade == 3 && (displayTypeCheck()) && (
							<TabPane tab="互动" key="2" forceRender={true}>
								<div id="wrap" className={style.messageWrap} ref={this.wrap} onScroll={this.handleScroll}>
									{list &&
										list.map((item, index) => {
											return (
												<div className={style.messageItemWrap} key={`message${index}`}>
													<div className={style.avator}>
														<img src={defaultAvator} />
													</div>
													<div className={style.item}>
														<div className={style.name}>{item.UserTrueName}</div>
														{messContent(item)}
													</div>
												</div>
											);
										})}
								</div>
								<div className={style.inputWrap}>
									<TextArea
										className={style.messInput}
										maxLength={300}
										onChange={this.handleAnswerChange}
										onKeyPress={this.handlePressEnter}
										placeholder="请输入消息"
										value={message}
									/>
									<Button className={style.submitBtn} onClick={this.handleSubmitMessage}>
										发送
									</Button>
								</div>
							</TabPane>
						)}
					</Tabs>
				</div>
			</div>
		);
	}
}
