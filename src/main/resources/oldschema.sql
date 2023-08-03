drop table nnwdaf_events_subscription;
drop table event_subscription;
drop table threshold_level;
drop table event_reporting_requirement;
drop table network_area_info;
drop table nsi_id_info;
drop table qos_requirement;
drop table retainability_threshold;
drop table snssai;
drop table target_ue_information;
drop table network_perf_requirement;
drop table bw_requirement;
drop table exception;
drop table expected_ue_behaviour_data;
drop table rat_freq_information;
drop table dispersion_requirement;
drop table redundant_transmission_exp_req;
drop table wlan_performance_req;
drop table upf_information;
drop table addr_fqdn;
drop table dn_performance_req;

CREATE TABLE IF NOT EXISTS threshold_level(
    nf_load_level INTEGER,
    nf_cpu_usage INTEGER,
    nf_memory_usage INTEGER,
    nf_storage_usage INTEGER,
    cong_level INTEGER,
    avg_traffic_rate INTEGER,
    max_traffic_rate INTEGER,
    avg_packet_delay INTEGER,
    max_packet_delay INTEGER,
);
CREATE TABLE IF NOT EXISTS event_reporting_requirement(

);
CREATE TABLE IF NOT EXISTS network_area_info(

);
CREATE TABLE IF NOT EXISTS nsi_id_info(

);
CREATE TABLE IF NOT EXISTS qos_requirement(

);
CREATE TABLE IF NOT EXISTS retainability_threshold(

);
CREATE TABLE IF NOT EXISTS snssai(

);
CREATE TABLE IF NOT EXISTS target_ue_information(

);
CREATE TABLE IF NOT EXISTS network_perf_requirement(

);
CREATE TABLE IF NOT EXISTS bw_requirement(

);
CREATE TABLE IF NOT EXISTS exception(

);
CREATE TABLE IF NOT EXISTS expected_ue_behaviour_data(

);
CREATE TABLE IF NOT EXISTS rat_freq_information(

);
CREATE TABLE IF NOT EXISTS dispersion_requirement(

);
CREATE TABLE IF NOT EXISTS redundant_transmission_exp_req(

);
CREATE TABLE IF NOT EXISTS wlan_performance_req(

);
CREATE TABLE IF NOT EXISTS upf_information(

);
CREATE TABLE IF NOT EXISTS addr_fqdn(

);
CREATE TABLE IF NOT EXISTS dn_performance_req(

);
CREATE TABLE IF NOT EXISTS event_subscription(
    any_slice Boolean,
    app_ids VARCHAR(255)[],
    dnns VARCHAR(255)[],
    dnais VARCHAR(255)[],
    "event" VARCHAR(50),
    extra_report_req event_reporting_requirement[],
    ladn_dnns VARCHAR(255)[],
    load_level_threshold INTEGER,
    notification_method VARCHAR(50),
    matching_dir VARCHAR(50),
    nf_load_lvl_thds threshold_level[],
    nf_instance_ids UUID[],
    nf_set_ids VARCHAR(255)[],
    nf_types VARCHAR(50)[],
    network_area network_area_info,
    visited_areas network_area_info[],
    max_top_app_ul_nbr INTEGER,
    max_top_app_dl_nbr INTEGER,
    nsi_id_infos nsi_id_info[],
    nsi_level_thrds INTEGER[],
    qos_requ qos_requirement,
    qos_flow_ret_thds retainability_threshold[],
    ran_ue_throu_thds VARCHAR(255)[],
    repetition_period INTEGER,
    snssaia snssai[],
    tgt_ue target_ue_information,
    cong_thresholds threshold_level[],
    nw_perf_requs network_perf_requirement[],
    bw_requs bw_requirement[],
    excep_requs exception[],
    expt_ana_type VARCHAR(50),
    expt_ue_behav expected_ue_behaviour_data,
    rat_freqs rat_freq_information[],
    list_of_ana_subsets varchar(50)[],
    disper_reqs dispersion_requirement[],
    red_trans_reqs redundant_transmission_exp_req[],
    wlan_reqs wlan_performance_req[],
    upf_info upf_information,
    app_server_addrs addr_fqdn[],
    dn_perf_reqs dn_performance_req[]
);

CREATE TABLE IF NOT EXISTS nnwdaf_events_subscription (
    id SERIAL PRIMARY KEY,
    event_subscriptions event_subscription[],
    notification_uri VARCHAR(255),
    notif_corr_id VARCHAR(255),
    supported_features VARCHAR(255)
);