<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="培训班" required>
                <j-search-select-tag
                  ref="selectTag"
                  v-model="queryParam.classNo"
                  :triggerChange="true"
                  placeholder="请选择培训班"
                  dict="training_class,name,no"
                  :disabled="false"
                  @change="loadData()"
                />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="课程名称">
                <j-input placeholder="请输入课程名称" v-model="queryParam.courseName"></j-input>
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
        <a-button @click="handleEdit({ classNo: queryParam.classNo })" type="primary" icon="plus">新增</a-button>
        <a-button type="primary" icon="download" @click="handleExportXls('课程表')">导出</a-button>
        <a-upload
          name="file"
          :showUploadList="false"
          :multiple="false"
          :headers="tokenHeader"
          :action="importExcelUrl"
          @change="handleImportExcel"
        >
          <a-button type="primary" icon="import">导入</a-button>
        </a-upload>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
          </a-menu>
          <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
        </a-dropdown>
      </div>

      <!-- table区域-begin -->
      <div v-if="queryParam.classNo">
        <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
          <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
          <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
          >项
          <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        </div>

        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          class="j-table-force-nowrap"
          :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
          @change="handleTableChange"
        >
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>

            <a-divider type="vertical" />
            <a-dropdown>
              <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </span>
        </a-table>
      </div>
      <a-empty v-else description="请先选择一个培训班，再进行操作" />
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <trainingClassSchedule-modal ref="modalForm" @ok="modalFormOk"></trainingClassSchedule-modal>
    </a-card>
  </page-layout>
</template>

<script>
import Vue from 'vue'
import '@/assets/less/TableExpand.less'
import PageLayout from '@/components/page/PageLayout'
import TrainingClassScheduleModal from './modules/TrainingClassScheduleModal'
import { getAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export default {
  name: 'TrainingClassScheduleList',
  mixins: [JeecgListMixin],
  components: {
    PageLayout,
    TrainingClassScheduleModal,
  },
  data() {
    return {
      description: '课程表管理页面',
      title: '任课教师',
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
          title: '星期',
          align: 'center',
          dataIndex: 'day_dictText',
          sorter: (a, b) => a.day - b.day,
        },
        {
          title: '节次',
          align: 'center',
          dataIndex: 'several_dictText',
        },
        {
          title: '课程名称',
          align: 'center',
          dataIndex: 'courseName',
          sorter: (a, b) => a.courseName - b.courseName,
        },
        {
          title: '教师',
          align: 'center',
          dataIndex: 'teacher_dictText',
          sorter: (a, b) => a.teacher - b.teacher,
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/training/trainingClassSchedule/list',
        delete: '/training/trainingClassSchedule/delete',
        deleteBatch: '/training/trainingClassSchedule/deleteBatch',
        exportXlsUrl: 'training/trainingClassSchedule/exportXls',
        importExcelUrl: 'training/trainingClassSchedule/importExcel',
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
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>