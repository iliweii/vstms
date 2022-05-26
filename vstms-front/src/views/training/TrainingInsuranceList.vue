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
              <a-form-item label="办理状态">
                <j-search-select-tag
                  v-model="queryParam.status"
                  :triggerChange="true"
                  placeholder="请选择办理状态"
                  dict="training_insurance_status"
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

      <!-- 隐藏的Table -->
      <a-table
        v-show="false"
        ref="table"
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        @change="handleTableChange"
      >
      </a-table>

      <!-- 区域-begin -->
      <div v-if="queryParam.classNo" :style="{ display: 'flex' }">
        <div v-for="item in dataSource" :key="item.id" :style="{ padding: '5px 10px' }">
          <a-card hoverable style="width: 150px">
            <img slot="cover" alt="头像" :src="item.avatar" v-if="item.avatar" />
            <img
              slot="cover"
              alt="头像"
              src="https://ss0.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=57917317,3261650226&fm=26&gp=0.jpg"
              v-else
            />
            <a-card-meta :title="item.username_dictText">
              <template slot="description">
                <span>当前：{{ item.status_dictText }}</span>
                <a-button
                  :type="item.status == '1' ? 'link' : 'primary'"
                  size="small"
                  @click="handleEdit(item)"
                  :icon="item.status == '1' ? 'check' : null"
                  >保险办理</a-button
                >
              </template>
            </a-card-meta>
          </a-card>
        </div>
      </div>
      <a-empty v-else description="请先选择一个培训班，再进行操作" />
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <trainingInsurance-modal ref="modalForm" @ok="modalFormOk"></trainingInsurance-modal>
    </a-card>
  </page-layout>
</template>

<script>
import Vue from 'vue'
import '@/assets/less/TableExpand.less'
import PageLayout from '@/components/page/PageLayout'
import TrainingInsuranceModal from './modules/TrainingInsuranceModal'
import { getAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export default {
  name: 'TrainingInsuranceList',
  mixins: [JeecgListMixin],
  components: {
    PageLayout,
    TrainingInsuranceModal,
  },
  data() {
    return {
      description: '保险办理管理页面',
      title: '培训班保险办理',
      url: {
        list: '/training/trainingInsurance/list',
        edit: '/training/trainingInsurance/edit',
        delete: '/training/trainingInsurance/delete',
        deleteBatch: '/training/trainingInsurance/deleteBatch',
        exportXlsUrl: 'training/trainingInsurance/exportXls',
        importExcelUrl: 'training/trainingInsurance/importExcel',
      },
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
      ],
      /* 分页参数 */
      ipagination: {
        current: 1,
        pageSize: -1,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
    }
  },
  mounted() {
    this.init()
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