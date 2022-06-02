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
              <a-form-item label="证书编号">
                <j-input placeholder="请输入证书编号" v-model="queryParam.no"></j-input>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="生成状态">
                <j-search-select-tag
                  v-model="queryParam.status"
                  :triggerChange="true"
                  placeholder="请选择生成状态"
                  dict="graduation_certificate_status"
                  :disabled="false"
                  @change="() => loadData()"
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
            <template v-if="toggleSearchStatus">
              <a-row>
                <a-col :xl="6" :lg="7" :md="8" :sm="24">
                  <a-form-item label="学员姓名">
                    <j-input placeholder="请输入学员姓名" v-model="queryParam.studentName"></j-input>
                  </a-form-item>
                </a-col>
              </a-row>
            </template>
          </a-row>
        </a-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="table-operator" v-if="queryParam.classNo">
        <!-- <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button> -->
        <a-button type="primary" icon="download" @click="handleExportXls('证书编号表')">导出</a-button>
        <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown> -->
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
          :rowKey="(r) => r.classNo + r.student"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          class="j-table-force-nowrap"
          :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
          @change="handleTableChange"
        >
          <!-- 编号 -->
          <span slot="no" slot-scope="text, record">
            <a v-if="!text" @click="handleGenerate(record)"> 点击生成合格证书 </a>
            <a v-else @click="handleCheck(record)">
              <strong>{{ text }}</strong>
            </a>
          </span>
          <!-- 状态 -->
          <span slot="status" slot-scope="text, record">
            <span style="color: red" v-if="record.status == '0'">{{ text }}</span>
            <span v-else> {{ text }}</span>
          </span>
          <!-- 操作栏 -->
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)" v-if="record.status == '0'">编辑</a>
            <a @click="handleDetail(record)" v-else>查看</a>

            <a-divider type="vertical" v-if="record.status == '1'" />
            <a @click="handleCheck(record)" v-if="record.status == '1'">证书</a>
          </span>
        </a-table>
      </div>
      <a-empty v-else description="请先选择一个培训班，再进行操作" />
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <graduationCertificate-modal ref="modalForm" @ok="modalFormOk"></graduationCertificate-modal>
      <graduationCert-modal ref="certModal" @ok="modalFormOk"></graduationCert-modal>
    </a-card>
  </page-layout>
</template>

<script>
import Vue from 'vue'
import '@/assets/less/TableExpand.less'
import PageLayout from '@/components/page/PageLayout'
import GraduationCertificateModal from './modules/GraduationCertificateModal'
import GraduationCertModal from './modules/GraduationCertModal'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { deleteAction, getAction, downFile, getFileAccessHttpUrl } from '@/api/manage'

export default {
  name: 'GraduationCertificateList',
  mixins: [JeecgListMixin],
  components: {
    PageLayout,
    GraduationCertificateModal,
    GraduationCertModal,
  },
  data() {
    return {
      description: '证书编号表管理页面',
      title: '证书编号',
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
          title: '证书编号',
          align: 'center',
          dataIndex: 'no',
          scopedSlots: { customRender: 'no' },
        },
        {
          title: '培训班编号',
          align: 'center',
          dataIndex: 'classNo',
        },
        {
          title: '学员用户名',
          align: 'center',
          dataIndex: 'student',
        },
        {
          title: '学员姓名',
          align: 'center',
          dataIndex: 'student_dictText',
        },
        {
          title: '状态',
          align: 'center',
          dataIndex: 'status_dictText',
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
        list: '/graduation/graduationCertificate/list',
        delete: '/graduation/graduationCertificate/delete',
        deleteBatch: '/graduation/graduationCertificate/deleteBatch',
        exportXlsUrl: 'graduation/graduationCertificate/exportXls',
        importExcelUrl: 'graduation/graduationCertificate/importExcel',
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
  },
  methods: {
    /**
     * 界面初始化时调用
     */
    init() {
      // 将用户习惯的查询取出
      Object.assign(this.queryParam, Vue.ls.get('USER_QUERY_CLASS_NO'))
      this.loadData()
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
    handleGenerate(record) {
      if (!record.studentId) {
        this.$message.warning('请先选择录入学员身份证号')
        return
      }
    },
    handleCheck(record) {
      this.$refs.certModal.showModal(record)
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>