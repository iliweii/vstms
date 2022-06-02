<template>
  <page-layout :title="title">
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
                  @change="() => loadData()"
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
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="table-operator" v-if="queryParam.classNo">
        <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
        <a-button type="primary" icon="download" @click="handleExportXls('评教')">导出</a-button>
        <!-- <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
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
          :rowKey="(a) => a.classNo + a.courseName"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          class="j-table-force-nowrap"
          :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
          @change="handleTableChange"
        >
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)" v-if="!record.score">评教</a>
            <a @click="handleDetail(record)" v-else>查看</a>
          </span>
          <span slot="score" slot-scope="text">
            <a-tag color="red" v-if="!text"> 还未进行评教，点击评教进入评教页面 </a-tag>
            <strong v-else>{{ text }}</strong>
          </span>
        </a-table>
      </div>
      <a-empty v-else description="请先选择一个培训班，再进行操作" />
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <graduationCommentTeach-modal ref="modalForm" @ok="modalFormOk"></graduationCommentTeach-modal>
    </a-card>
  </page-layout>
</template>

<script>
import Vue from 'vue'
import '@/assets/less/TableExpand.less'
import PageLayout from '@/components/page/PageLayout'
import GraduationCommentTeachModal from './modules/GraduationCommentTeachModal'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { deleteAction, getAction, downFile, getFileAccessHttpUrl } from '@/api/manage'

export default {
  name: 'GraduationCommentTeachList',
  mixins: [JeecgListMixin],
  components: {
    PageLayout,
    GraduationCommentTeachModal,
  },
  data() {
    return {
      description: '评教管理页面',
      title: '评教',
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
          title: '分数',
          align: 'center',
          dataIndex: 'score',
          scopedSlots: { customRender: 'score' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/graduation/graduationCommentTeach/list?type=student',
        delete: '/graduation/graduationCommentTeach/delete',
        deleteBatch: '/graduation/graduationCommentTeach/deleteBatch',
        exportXlsUrl: 'graduation/graduationCommentTeach/exportXls',
        importExcelUrl: 'graduation/graduationCommentTeach/importExcel',
      },
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {},
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>