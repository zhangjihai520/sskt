###魔题库精简版CSS 文档规范

1、模块CSS文件名与功能文件名称保持一致，文件位置需在同级，

2、Css命名方式
公用模块的以首字母加中线'-',
如：布局（grid）（.g-）；模块（module）（.m-）；元件（unit）（.u-）；功能（function）（.f-）；皮肤（skin）（.s-）；状态（.z-）。
备注：写在global.less文件里面的是要按照这个标准来，私有的less 只要m- 开头即可 其他随意
私有模块以驼峰模式命名，如：.modulSearch。
需建一个大容器（如：m-search），其它样式都包裹在内，防止打包时名称相同而污染其它文件。
<code>
        .m-search{
            .search-opt{
                .search-pic{
                color: #f7f7f7;
                }
                .icon-del{
                font-size: 16px;
                }
            }
        }
        <div className={styles.m-search}>
          <div className={styles.search-opt}>
              <div className={styles.search-pic}></div>
              <div className={styles.icon-del}>
                <div className={styles.tip}></div>
              </div>
          </div>
        </div>
</code>

3、页面内的全局样式需要放在大容器内或其父级样式内如下图，防止样式溢出污染其它文件

<code>
    .search-opt{
        :glbal{
            .tip{
            margin-top: 5px;
            }
        }
    }
</code>

4、色值后缀有_p表示色调加深(plus)，后缀有_m表示色调减淡(minus)
    .grayColor（灰）  .grayColorP（深灰） .grayColorM（淡灰）


5、统一语义理解和命名

布局（.g-）
语义	命名	简写
文档	doc	doc
头部	head	hd
主体	body	bd
尾部	foot	ft
主栏	main	mn
主栏子容器	mainc	mnc
侧栏	side	sd
侧栏子容器	sidec	sdc
盒容器	wrap/box	wrap/box



模块（.m-）、元件（.u-）
语义	命名	简写
导航	nav	nav
子导航	subnav	snav
面包屑	crumb	crm
菜单	menu	menu
选项卡	tab	tab
标题区	head/title	hd/tt
内容区	body/content	bd/ct
列表	list	lst
表格	table	tb
表单	form	fm
热点	hot	hot
排行	top	top
登录	login	log
标志	logo	logo
广告	advertise	ad
搜索	search	sch
幻灯	slide	sld
提示	tips	tips
帮助	help	help
新闻	news	news
下载	download	dld
注册	regist	reg
投票	vote	vote
版权	copyright	cprt
结果	result	rst
标题	title	tt
按钮	button	btn
输入	input	ipt


皮肤（.s-）

语义	命名	简写
字体颜色	fontcolor	fc
背景	background	bg
背景颜色	backgroundcolor	bgc
背景图片	backgroundimage	bgi
背景定位	backgroundposition	bgp
边框颜色	bordercolor	bdc

