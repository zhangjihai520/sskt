import Link from 'umi/link';
import withBreadcrumbs from 'react-router-breadcrumbs-hoc';
import { Breadcrumb, Badge, Icon } from 'antd';
import pathToRegexp from 'path-to-regexp';

/**
 * 嵌套json转成数组
 */
function translateToArray(arJson, data) {
  arJson.push({
    documentTitle: data.documentTitle || '',
    exact: data.exact || false,
    isLink: data.isLink || false,
    breadcrumb: data.name,
    path: data.path
  })
  if(data.parent){
    translateToArray(arJson, data.parent);
  }
}

const Breadcrumbs = ({ data, location, LastBreadcrumbName }) => {
  let breadcrumbsArray = [];
  const pathname = location.pathname.split('?')[0];
  const current = data.filter(item => {
    return item.path === pathname || pathToRegexp(item.path).test(pathname)
  })[0];
  // 标题
  let titleArr = current.documentTitle.split(' | ');
  if (LastBreadcrumbName) {
    titleArr = titleArr.map((item, index) => {
      return index === 0 ? LastBreadcrumbName : item
    })
  }
  document.title = titleArr.join(' | ')

  translateToArray(breadcrumbsArray, current);

  const AntdBreadcrumb = withBreadcrumbs(breadcrumbsArray, { disableDefaults: true })(({ breadcrumbs }) => {
    return (
      <Breadcrumb separator="/" classNames="spread">
        {
          breadcrumbs.map((breadcrumb, index) => {
            return (
            <Breadcrumb.Item key={breadcrumb.key}>
              {
                breadcrumbs.length - 1 === index ? (LastBreadcrumbName ? LastBreadcrumbName: breadcrumb.breadcrumb):
                (
                  <Link
                    to={{
                      pathname: breadcrumb.match.url,
                      state: breadcrumb.location.state ? breadcrumb.location.state:{},
                      query: breadcrumb.location.query ? breadcrumb.location.query : {},
                  }}
                >
                  { index === 0 ? '首页' : breadcrumb.breadcrumb }
                </Link>
                )
              }
            </Breadcrumb.Item>
          )})
        }
      </Breadcrumb>
    )
  });
  return <AntdBreadcrumb />;
};

export default Breadcrumbs;
