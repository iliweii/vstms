<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="培训班">
              <j-search-select-tag
                v-model="queryParam.classNo"
                :triggerChange="true"
                placeholder="请选择培训班"
                dict="training_class,name,no"
                :disabled="false"
                @change="
                  () => {
                    loadData()
                    getCourseList()
                  }
                "
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="课程名称">
              <j-search-select-tag
                v-model="queryParam.courseName"
                placeholder="请选择课程名称"
                :dictOptions="courseDictOptions"
                :disabled="false"
                @change="loadData()"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="录入状态">
              <j-search-select-tag
                v-model="queryParam.status"
                :triggerChange="true"
                placeholder="请选择录入状态"
                dict="training_course_score_status"
                :disabled="false"
                @change="loadData()"
              />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
          <template v-if="toggleSearchStatus"> </template>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator" v-if="queryParam.classNo">
      <!-- <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button> -->
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('成绩管理')">导出</a-button> -->
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
        :disabled="!queryParam.courseName"
      >
        <a-button type="primary" icon="import" :disabled="!queryParam.courseName">导入</a-button>
      </a-upload>
      <a-button
        type="primary"
        icon="download"
        @click="handleExportTemp(queryParam.courseName + '成绩表')"
        :disabled="!queryParam.courseName"
        >导出成绩表模板</a-button
      >
      <a-button
        type="primary"
        icon="download"
        @click="handleExportError('成绩表导入错误信息')"
        :disabled="!queryParam.courseName"
        >导出错误信息</a-button
      >
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>

      <a-alert message="请选择课程名称后操作" type="info" v-if="!queryParam.courseName" show-icon />
    </div>

    <!-- table区域-begin -->
    <div v-if="queryParam.classNo">
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
        <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
        >项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div> -->

      <a-table
        ref="table"
        size="middle"
        bordered
        :rowKey="(e) => e.classNo + '-' + e.courseName + '-'"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <span slot="status" slot-scope="text, record">
          <a-tag :color="record.status == '0' ? 'red' : 'green'"> {{ text }} </a-tag>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)" :disabled="record.status == '0'">{{
            record.status == '0' ? '导入后可查看' : '查看'
          }}</a>
        </span>
      </a-table>
    </div>
    <a-empty v-else description="请先选择一个培训班，再进行操作" />
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <trainingScore-modal ref="modalForm" @ok="modalFormOk"></trainingScore-modal>
  </a-card>
</template>

<script>
import Vue from 'vue'
import '@/assets/less/TableExpand.less'
import TrainingScoreModal from './modules/TrainingScoreList'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { deleteAction, getAction, downFile, getFileAccessHttpUrl } from '@/api/manage'

export default {
  name: 'TrainingScoreList',
  mixins: [JeecgListMixin],
  components: {
    TrainingScoreModal,
  },
  data() {
    return {
      description: '成绩管理管理页面',
      isorter: {
        column: 'courseName',
        order: 'asc',
      },
      // 数据
      courseDictOptions: [], // 课程下拉框list
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '培训班编号',
          align: 'center',
          dataIndex: 'classNo',
        },
        {
          title: '课程名称',
          align: 'center',
          dataIndex: 'courseName',
        },
        {
          title: '教师信息',
          align: 'center',
          dataIndex: 'teacher_dictText',
        },
        {
          title: '录入状态',
          align: 'center',
          dataIndex: 'status_dictText',
          sorter: (a, b) => a.status - b.status,
          scopedSlots: { customRender: 'status' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/training/trainingScore/courseList',
        exportXlsUrl: 'training/trainingScore/exportXls',
        errorXls: 'training/trainingScore/errorXls',
        exportTemplate: 'training/trainingScore/exportTemplate',
        importExcelUrl: 'training/trainingScore/importExcel',
        courseList: '/training/trainingClassSchedule/courseList', // 课程列表
      },
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  created() {
    this.init()
    this.loadData()
  },
  methods: {
    /**
     * 界面初始化时调用
     */
    init() {
      // 将用户习惯的查询取出
      Object.assign(this.queryParam, Vue.ls.get('USER_QUERY_CLASS_NO'))
      // 加载字典下拉框数据
      this.getCourseList()
    },
    /**
     * 异步获取课程list，并赋值到下拉选择列表courseDictOptions中
     */
    async getCourseList() {
      if (!this.queryParam.classNo) return
      const { result } = await getAction(this.url.courseList, {
        classNo: this.queryParam.classNo,
      })
      this.courseDictOptions = result.map((e) => {
        return {
          text: e,
          value: e,
        }
      })
    },
    /**
     * 加载数据
     * @param arg 参数
     */
    loadData(arg) {
      if (arg === 1) {
        this.ipagination.current = 1
      }
      var params = this.getQueryParams() //查询条件
      // 将查询条件存储到本地
      if (params.classNo) Vue.ls.set('USER_QUERY_CLASS_NO', { classNo: params.classNo })
      else return
      this.loading = true
      getAction(this.url.list, params)
        .then((res) => {
          if (res.success) {
            this.dataSource = res.result.records || res.result
            if (res.result.total) {
              this.ipagination.total = res.result.total
            } else {
              this.ipagination.total = 0
            }
          } else {
            this.$message.warning(res.message)
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    handleExportTemp(fileName) {
      let param = this.getQueryParams()
      console.log('导出参数', param)
      downFile(this.url.exportTemplate, param).then((data) => {
        if (!data) {
          this.$message.warning('文件下载失败')
          return
        }
        if (typeof window.navigator.msSaveBlob !== 'undefined') {
          window.navigator.msSaveBlob(new Blob([data], { type: 'application/vnd.ms-excel' }), fileName + '.xls')
        } else {
          let url = window.URL.createObjectURL(new Blob([data], { type: 'application/vnd.ms-excel' }))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', fileName + '.xls')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link) //下载完成移除元素
          window.URL.revokeObjectURL(url) //释放掉blob对象
        }
      })
    },
    handleExportError(fileName) {
      let param = this.getQueryParams()
      console.log('导出参数', param)
      downFile(this.url.errorXls, param).then((data) => {
        if (!data) {
          this.$message.warning('文件下载失败')
          return
        } else if (data.type === 'application/json') {
          this.$message.warning('暂无错误信息')
          return
        }
        if (typeof window.navigator.msSaveBlob !== 'undefined') {
          window.navigator.msSaveBlob(new Blob([data], { type: 'application/vnd.ms-excel' }), fileName + '.xls')
        } else {
          let url = window.URL.createObjectURL(new Blob([data], { type: 'application/vnd.ms-excel' }))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', fileName + '.xls')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link) //下载完成移除元素
          window.URL.revokeObjectURL(url) //释放掉blob对象
        }
      })
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>