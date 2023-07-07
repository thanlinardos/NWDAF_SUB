package io.nwdaf.eventsubscription.requestbuilders;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.util.Pair;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.nwdaf.eventsubscription.model.Accuracy;
import io.nwdaf.eventsubscription.model.Accuracy.AccuracyEnum;
import io.nwdaf.eventsubscription.model.AddrFqdn;
import io.nwdaf.eventsubscription.model.AnalyticsMetadata;
import io.nwdaf.eventsubscription.model.AnalyticsMetadata.AnalyticsMetadataEnum;
import io.nwdaf.eventsubscription.model.DatasetStatisticalProperty.DatasetStatisticalPropertyEnum;
import io.nwdaf.eventsubscription.model.DispersionClass.DispersionClassEnum;
import io.nwdaf.eventsubscription.model.DispersionClass;
import io.nwdaf.eventsubscription.model.DispersionOrderingCriterion;
import io.nwdaf.eventsubscription.model.DispersionOrderingCriterion.DispersionOrderingCriterionEnum;
import io.nwdaf.eventsubscription.model.Ecgi;
import io.nwdaf.eventsubscription.model.EllipsoidArc;
import io.nwdaf.eventsubscription.model.Ncgi;
import io.nwdaf.eventsubscription.model.GlobalRanNodeId;
import io.nwdaf.eventsubscription.model.IpAddr;
import io.nwdaf.eventsubscription.model.LocationArea;
import io.nwdaf.eventsubscription.model.MatchingDirection;
import io.nwdaf.eventsubscription.model.MatchingDirection.MatchingDirectionEnum;
import io.nwdaf.eventsubscription.model.NsiIdInfo;
import io.nwdaf.eventsubscription.model.AnalyticsMetadataIndication;
import io.nwdaf.eventsubscription.model.AnalyticsSubset;
import io.nwdaf.eventsubscription.model.AnalyticsSubset.AnalyticsSubsetEnum;
import io.nwdaf.eventsubscription.model.BatteryIndication;
import io.nwdaf.eventsubscription.model.BwRequirement;
import io.nwdaf.eventsubscription.model.CivicAddress;
import io.nwdaf.eventsubscription.model.ClassCriterion;
import io.nwdaf.eventsubscription.model.ConsumerNfInformation;
import io.nwdaf.eventsubscription.model.DatasetStatisticalProperty;
import io.nwdaf.eventsubscription.model.DispersionRequirement;
import io.nwdaf.eventsubscription.model.DispersionType;
import io.nwdaf.eventsubscription.model.DispersionType.DispersionTypeEnum;
import io.nwdaf.eventsubscription.model.DnPerfOrderingCriterion;
import io.nwdaf.eventsubscription.model.DnPerfOrderingCriterion.DnPerfOrderingCriterionEnum;
import io.nwdaf.eventsubscription.model.DnPerformanceReq;
import io.nwdaf.eventsubscription.model.EventReportingRequirement;
import io.nwdaf.eventsubscription.model.EventSubscription;
import io.nwdaf.eventsubscription.model.Exception;
import io.nwdaf.eventsubscription.model.ExceptionId;
import io.nwdaf.eventsubscription.model.ExceptionId.ExceptionIdEnum;
import io.nwdaf.eventsubscription.model.ExceptionTrend;
import io.nwdaf.eventsubscription.model.ExceptionTrend.ExceptionTrendEnum;
import io.nwdaf.eventsubscription.model.ExpectedAnalyticsType;
import io.nwdaf.eventsubscription.model.ExpectedAnalyticsType.ExpectedAnalyticsTypeEnum;
import io.nwdaf.eventsubscription.model.ExpectedUeBehaviourData;
import io.nwdaf.eventsubscription.model.GNbId;
import io.nwdaf.eventsubscription.model.GeographicArea;
import io.nwdaf.eventsubscription.model.GeographicalCoordinates;
import io.nwdaf.eventsubscription.model.NFType;
import io.nwdaf.eventsubscription.model.NFType.NFTypeEnum;
import io.nwdaf.eventsubscription.model.NetworkAreaInfo;
import io.nwdaf.eventsubscription.model.NetworkAreaInfo1;
import io.nwdaf.eventsubscription.model.NetworkPerfRequirement;
import io.nwdaf.eventsubscription.model.NetworkPerfType;
import io.nwdaf.eventsubscription.model.NetworkPerfType.NetworkPerfTypeEnum;
import io.nwdaf.eventsubscription.model.NnwdafEventsSubscription;
import io.nwdaf.eventsubscription.model.NotificationFlag;
import io.nwdaf.eventsubscription.model.NotificationFlag.NotificationFlagEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod;
import io.nwdaf.eventsubscription.model.NwdafEvent;
import io.nwdaf.eventsubscription.model.TargetUeInformation;
import io.nwdaf.eventsubscription.model.ThresholdLevel;
import io.nwdaf.eventsubscription.model.TimeUnit;
import io.nwdaf.eventsubscription.model.TimeUnit.TimeUnitEnum;
import io.nwdaf.eventsubscription.model.TimeWindow;
import io.nwdaf.eventsubscription.model.TrafficProfile;
import io.nwdaf.eventsubscription.model.TrafficProfile.TrafficProfileEnum;
import io.nwdaf.eventsubscription.model.NotificationMethod.NotificationMethodEnum;
import io.nwdaf.eventsubscription.model.NwdafEvent.NwdafEventEnum;
import io.nwdaf.eventsubscription.model.OutputStrategy;
import io.nwdaf.eventsubscription.model.OutputStrategy.OutputStrategyEnum;
import io.nwdaf.eventsubscription.model.PartitioningCriteria;
import io.nwdaf.eventsubscription.model.PartitioningCriteria.PartitioningCriteriaEnum;
import io.nwdaf.eventsubscription.model.PlmnId;
import io.nwdaf.eventsubscription.model.Point;
import io.nwdaf.eventsubscription.model.PointAltitude;
import io.nwdaf.eventsubscription.model.PointAltitudeUncertainty;
import io.nwdaf.eventsubscription.model.PointList;
import io.nwdaf.eventsubscription.model.PointUncertaintyCircle;
import io.nwdaf.eventsubscription.model.PointUncertaintyEllipse;
import io.nwdaf.eventsubscription.model.Polygon;
import io.nwdaf.eventsubscription.model.PrevSubInfo;
import io.nwdaf.eventsubscription.model.QosRequirement;
import io.nwdaf.eventsubscription.model.QosResourceType;
import io.nwdaf.eventsubscription.model.QosResourceType.QosResourceTypeEnum;
import io.nwdaf.eventsubscription.model.RankingCriterion;
import io.nwdaf.eventsubscription.model.RatFreqInformation;
import io.nwdaf.eventsubscription.model.RatType;
import io.nwdaf.eventsubscription.model.RatType.RatTypeEnum;
import io.nwdaf.eventsubscription.model.RedTransExpOrderingCriterion;
import io.nwdaf.eventsubscription.model.RedTransExpOrderingCriterion.RedTransExpOrderingCriterionEnum;
import io.nwdaf.eventsubscription.model.StationaryIndication.StationaryIndicationEnum;
import io.nwdaf.eventsubscription.model.SupportedGADShapes;
import io.nwdaf.eventsubscription.model.SupportedGADShapes.SupportedGADShapesEnum;
import io.nwdaf.eventsubscription.model.RedundantTransmissionExpReq;
import io.nwdaf.eventsubscription.model.ReportingInformation;
import io.nwdaf.eventsubscription.model.RetainabilityThreshold;
import io.nwdaf.eventsubscription.model.ScheduledCommunicationTime1;
import io.nwdaf.eventsubscription.model.ScheduledCommunicationType;
import io.nwdaf.eventsubscription.model.ScheduledCommunicationType.ScheduledCommunicationTypeEnum;
import io.nwdaf.eventsubscription.model.Snssai;
import io.nwdaf.eventsubscription.model.StationaryIndication;
import io.nwdaf.eventsubscription.model.Tai;
import io.nwdaf.eventsubscription.model.UeAnalyticsContextDescriptor;
import io.nwdaf.eventsubscription.model.UmtTime;
import io.nwdaf.eventsubscription.model.UncertaintyEllipse;
import io.nwdaf.eventsubscription.model.UpfInformation;
import io.nwdaf.eventsubscription.model.WlanOrderingCriterion;
import io.nwdaf.eventsubscription.model.WlanOrderingCriterion.WlanOrderingCriterionEnum;
import io.nwdaf.eventsubscription.model.WlanPerformanceReq;

public class CreateSubscriptionRequestBuilder {
	

	public NnwdafEventsSubscription InitSubscriptionRequest(String clientURI) {
		NnwdafEventsSubscription bodyObject = new NnwdafEventsSubscription();
		bodyObject.notificationURI(clientURI);
		
		return bodyObject;
	}

	public NnwdafEventsSubscription AddEventToSubscription(NnwdafEventsSubscription sub,String event,String notifMethod,List<String>optionals,
			List<String> anaMeta,List<String> anaMetaInd,List<String> dataStatProps,List<String> accPerSubset,
			List<String>args,List<List<String>>nfLoadLvlThds, List<String> supis, List<String> intGroupIds, List<String> nfInstanceIds, List<String> nfSetIds,
			List<String> appIds, List<String> dnns, List<String> dnais,List<String> ladnDnns,List<String> nfTypes,
			List<List<List<List<String>>>> visitedAreas,List<List<List<String>>> nsiIdInfos,
			List<Integer> nsiLevelThrds,List<List<String>> qosFlowRetThds,List<String> ranUeThrouThds, List<List<String>> snssaia, List<List<String>> congThresholds,
			List<List<String>> nwPerfRequs,
			List<List<String>> bwRequs,List<List<String>> excepRequs,List<List<List<String>>> ratFreqs,List<String> listOfAnaSubsets,List<List<List<List<String>>>> disperReqs,
			List<List<String>> redTransReqs,List<List<List<String>>> wlanReqs,List<List<String>> appServerAddrs,List<List<List<List<String>>>> dnPerfReqs,
			List<List<List<String>>> networkArea,List<String> qosRequ,List<List<List<List<List<List<String>>>>>> exptUeBehav,List<String> upfInfo) {
		EventSubscription eventSub = new EventSubscription();	
		eventSub.event(new NwdafEvent().event(NwdafEventEnum.fromValue(event)));
		eventSub.notificationMethod(new NotificationMethod().notifMethod(NotificationMethodEnum.fromValue(notifMethod)));
		EventReportingRequirement extraRepReq = new EventReportingRequirement();
		if(optionals.size()>0) {
			if(optionals.get(0)!=null) {
				extraRepReq.maxObjectNbr(Integer.parseInt(optionals.get(0)));
				
			}
			if(optionals.get(1)!=null) {
				extraRepReq.maxSupiNbr(Integer.parseInt(optionals.get(1)));
			}
			if(optionals.get(2)!=null) {
				extraRepReq.startTs(OffsetDateTime.parse(optionals.get(2)));
			}
			if(optionals.get(3)!=null) {
				extraRepReq.endTs(OffsetDateTime.parse(optionals.get(3)));
			}
			if(optionals.get(4)!=null) {
				extraRepReq.accuracy(new Accuracy().accuracy(AccuracyEnum.fromValue(optionals.get(4))));
			}
			if(optionals.get(5)!=null) {
				extraRepReq.timeAnaNeeded(OffsetDateTime.parse(optionals.get(5)));
			}
			if(optionals.get(6)!=null) {
				extraRepReq.offsetPeriod(Integer.parseInt(optionals.get(6)));
			}
		}
		if(anaMeta.size()>0) {
			List<AnalyticsMetadata> l = new ArrayList<AnalyticsMetadata>();
			for(int i =0;i<anaMeta.size();i++) {
				l.add(new AnalyticsMetadata().anaMeta(AnalyticsMetadataEnum.fromValue(anaMeta.get(i))));
			}
			extraRepReq.anaMeta(l);
		}
		if(anaMetaInd.size()>0) {
			AnalyticsMetadataIndication anametaind = new AnalyticsMetadataIndication();
			TimeWindow tw = new TimeWindow();
			if(anaMetaInd.get(0)!=null) {
				tw.startTime(OffsetDateTime.parse(anaMetaInd.get(0)));
			}
			if(anaMetaInd.get(1)!=null) {
				tw.stopTime(OffsetDateTime.parse(anaMetaInd.get(1)));
			}
			if((anaMetaInd.get(0)!=null)||(anaMetaInd.get(1)!=null)) {
				anametaind.dataWindow(tw);
			}
			
			if(dataStatProps.size()>0) {
				for(int i=0;i<dataStatProps.size();i++) {
					anametaind.addDataStatPropsItem(new DatasetStatisticalProperty().dataStatProps(DatasetStatisticalPropertyEnum.fromValue(dataStatProps.get(i))));
				}
			}
			if(anaMetaInd.get(2)!=null) {
				anametaind.strategy(new OutputStrategy().strategy(OutputStrategyEnum.fromValue(anaMetaInd.get(2))));
			}
			if(anaMetaInd.get(3)!=null) {
				anametaind.addAggrNwdafIdsItem(UUID.fromString(anaMetaInd.get(3)));
			}
			extraRepReq.anaMetaInd(anametaind);
		}
		if(accPerSubset.size()>0) {
			for(int i=0;i<accPerSubset.size();i++) {
				extraRepReq.addAccPerSubsetItem(new Accuracy().accuracy(AccuracyEnum.fromValue(accPerSubset.get(i))));
			}
		}
		if(optionals.size()>0 || anaMeta.size()>0 || anaMetaInd.size()>0 || accPerSubset.size()>0) {
			eventSub.extraReportReq(extraRepReq);
		}
		TargetUeInformation tgtUe = new TargetUeInformation();
		if(supis.size()>0||intGroupIds.size()>0) {
			tgtUe.supis(supis).intGroupIds(intGroupIds);
		}
		if(args.get(0)!=null) {
			tgtUe.anyUe(args.get(0).equalsIgnoreCase("TRUE"));
		}
		if(supis.size()>0||intGroupIds.size()>0||args.get(0)!=null) {
			eventSub.tgtUe(tgtUe);
		}
		if(nfInstanceIds.size()>0) {
			for(int i=0;i<nfInstanceIds.size();i++) {
				eventSub.addNfInstanceIdsItem(UUID.fromString(nfInstanceIds.get(i)));
			}
		}
		if(nfSetIds.size()>0) {
			eventSub.nfSetIds(nfSetIds);
		}
		if(appIds.size()>0) {
			eventSub.appIds(appIds);
		}
		if(dnns.size()>0) {
			eventSub.dnns(dnns);
		}
		if(dnais.size()>0) {
			eventSub.dnais(dnais);
		}
		if(ladnDnns.size()>0) {
			eventSub.ladnDnns(ladnDnns);
		}
		if(nfTypes.size()>0) {
			for(int i=0;i<nfTypes.size();i++) {
				eventSub.addNfTypesItem(new NFType().nfType(NFTypeEnum.fromValue(nfTypes.get(i))));
			}
		}
		if(nfLoadLvlThds.size()>0) {
			for(int i=0;i<nfLoadLvlThds.size();i++) {
				eventSub.addNfLoadLvlThdsItem(new ThresholdLevel().congLevel(Integer.parseInt(nfLoadLvlThds.get(i).get(0))).nfLoadLevel(Integer.parseInt(nfLoadLvlThds.get(i).get(1))).nfCpuUsage(Integer.parseInt(nfLoadLvlThds.get(i).get(2))).nfMemoryUsage(Integer.parseInt(nfLoadLvlThds.get(i).get(3))).nfStorageUsage(Integer.parseInt(nfLoadLvlThds.get(i).get(4))).avgTrafficRate(nfLoadLvlThds.get(i).get(5)).maxTrafficRate(nfLoadLvlThds.get(i).get(6)).avgPacketDelay(Integer.parseInt(nfLoadLvlThds.get(i).get(7))).maxPacketDelay(Integer.parseInt(nfLoadLvlThds.get(i).get(8))).avgPacketLossRate(Integer.parseInt(nfLoadLvlThds.get(i).get(9))).svcExpLevel(Float.parseFloat(nfLoadLvlThds.get(i).get(10))));
			}
			
		}
		if(visitedAreas.size()>0) {
			for(int n=0;n<visitedAreas.size();n++) {
				NetworkAreaInfo area = new NetworkAreaInfo();
				for(int i=0;i<visitedAreas.get(n).get(0).size();i++) {
					Ecgi ecgi = new Ecgi();
					if(visitedAreas.get(n).get(0).get(i).get(0)!=null&&visitedAreas.get(n).get(0).get(i).get(1)!=null) {
						ecgi.plmnId(new PlmnId().mcc(visitedAreas.get(n).get(0).get(i).get(0)).mnc(visitedAreas.get(n).get(0).get(i).get(1)));
					}
					ecgi.eutraCellId(visitedAreas.get(n).get(0).get(i).get(2)).nid(visitedAreas.get(n).get(0).get(i).get(3));
					area.addEcgisItem(ecgi);
				}
				
				for(int i=0;i<visitedAreas.get(n).get(1).size();i++) {
					Ncgi ncgi = new Ncgi();
					if(visitedAreas.get(n).get(1).get(i).get(1)!=null&&visitedAreas.get(n).get(1).get(i).get(1)!=null) {
						ncgi.plmnId(new PlmnId().mcc(visitedAreas.get(n).get(1).get(i).get(0)).mnc(visitedAreas.get(n).get(1).get(i).get(1)));
					}
					ncgi.nrCellId(visitedAreas.get(n).get(1).get(i).get(2)).nid(visitedAreas.get(n).get(1).get(i).get(3));
					area.addNcgisItem(ncgi);
				}
				
				for(int i=0;i<visitedAreas.get(n).get(2).size();i++) {
					GlobalRanNodeId gRanNodeId = new GlobalRanNodeId();
					if(visitedAreas.get(n).get(2).get(i).get(1)!=null&&visitedAreas.get(n).get(2).get(i).get(1)!=null) {
						gRanNodeId.plmnId(new PlmnId().mcc(visitedAreas.get(n).get(2).get(i).get(0)).mnc(visitedAreas.get(n).get(2).get(i).get(1)));
					}
					gRanNodeId.n3IwfId(visitedAreas.get(n).get(2).get(i).get(2)).ngeNbId(visitedAreas.get(n).get(2).get(i).get(3))
																		.wagfId(visitedAreas.get(n).get(2).get(i).get(4))
																		.tngfId(visitedAreas.get(n).get(2).get(i).get(5))
																		.nid(visitedAreas.get(n).get(2).get(i).get(6))
																		.eNbId(visitedAreas.get(n).get(2).get(i).get(7));
					if(visitedAreas.get(n).get(2).get(i).get(8)!=null&&visitedAreas.get(n).get(2).get(i).get(9)!=null) {
						gRanNodeId.gNbId(new GNbId().bitLength(Integer.parseInt(visitedAreas.get(n).get(2).get(i).get(8))).gNBValue(visitedAreas.get(n).get(2).get(i).get(9)));
					}
					area.addGRanNodeIdsItem(gRanNodeId);
				}
				
				for(int i=0;i<visitedAreas.get(n).get(3).size();i++) {
					Tai tai = new Tai();
					if(visitedAreas.get(n).get(3).get(i).get(1)!=null&&visitedAreas.get(n).get(3).get(i).get(1)!=null) {
						tai.plmnId(new PlmnId().mcc(visitedAreas.get(n).get(3).get(i).get(0)).mnc(visitedAreas.get(n).get(3).get(i).get(1)));
					}
					tai.tac(visitedAreas.get(n).get(3).get(i).get(2)).nid(visitedAreas.get(n).get(3).get(i).get(3));
					area.addTaisItem(tai);
				}
				eventSub.addVisitedAreasItem(area);
			}
		}
		if(nsiIdInfos.size()>0) {
			for(int n=0;n<nsiIdInfos.size();n++) {
				NsiIdInfo nsiIdInfo = new NsiIdInfo();
				if(nsiIdInfos.get(n).get(0).get(0)!=null) {
					nsiIdInfo.snssai(new Snssai().sst(Integer.parseInt(nsiIdInfos.get(n).get(0).get(0))).sd(nsiIdInfos.get(n).get(0).get(1)));
				}
				nsiIdInfo.nsiIds(nsiIdInfos.get(n).get(1));
				eventSub.addNsiIdInfosItem(nsiIdInfo);
			}
		}
		if(nsiLevelThrds.size()>0) {
			eventSub.nsiLevelThrds(nsiLevelThrds);
		}
		if(qosFlowRetThds.size()>0) {
			for(int i=0;i<qosFlowRetThds.size();i++) {
				if(qosFlowRetThds.get(i).get(0)!=null&&qosFlowRetThds.get(i).get(1)!=null&&qosFlowRetThds.get(i).get(2)!=null) {
					eventSub.addQosFlowRetThdsItem(new RetainabilityThreshold().relFlowNum(Integer.parseInt(qosFlowRetThds.get(i).get(0))).relFlowRatio(Integer.parseInt(qosFlowRetThds.get(i).get(1))).relTimeUnit(new TimeUnit().relTimeUnit(TimeUnitEnum.fromValue(qosFlowRetThds.get(i).get(2)))));
				}
			}
		}
		if(ranUeThrouThds.size()>0) {
			eventSub.ranUeThrouThds(ranUeThrouThds);
		}
		if(snssaia.size()>0) {
			for(int i=0;i<snssaia.size();i++) {
				eventSub.addSnssaiaItem(new Snssai().sst(Integer.parseInt(snssaia.get(i).get(0))).sd(snssaia.get(i).get(1)));
			}
		}
		if(congThresholds.size()>0) {
			for(int i=0;i<congThresholds.size();i++) {
				eventSub.addCongThresholdsItem(new ThresholdLevel().congLevel(Integer.parseInt(congThresholds.get(i).get(0))).nfLoadLevel(Integer.parseInt(congThresholds.get(i).get(1))).nfCpuUsage(Integer.parseInt(congThresholds.get(i).get(2))).nfMemoryUsage(Integer.parseInt(congThresholds.get(i).get(3))).nfStorageUsage(Integer.parseInt(congThresholds.get(i).get(4))).avgTrafficRate(congThresholds.get(i).get(5)).maxTrafficRate(congThresholds.get(i).get(6)).avgPacketDelay(Integer.parseInt(congThresholds.get(i).get(7))).maxPacketDelay(Integer.parseInt(congThresholds.get(i).get(8))).avgPacketLossRate(Integer.parseInt(congThresholds.get(i).get(9))).svcExpLevel(Float.parseFloat(congThresholds.get(i).get(10))));
			}
			
		}
		if(nwPerfRequs.size()>0) {
			for(int i=0;i<nwPerfRequs.size();i++) {
				if(nwPerfRequs.get(i).get(0)!=null&&nwPerfRequs.get(i).get(1)!=null&&nwPerfRequs.get(i).get(2)!=null) {
					eventSub.addNwPerfRequsItem(new NetworkPerfRequirement().nwPerfType(new NetworkPerfType().nwPerfType(NetworkPerfTypeEnum.fromValue(nwPerfRequs.get(i).get(0)))).relativeRatio(Integer.parseInt(nwPerfRequs.get(i).get(1))).absoluteNum(Integer.parseInt(nwPerfRequs.get(i).get(2))));
				}
			}
		}
		if(bwRequs.size()>0) {
			for(int i=0;i<bwRequs.size();i++) {
				eventSub.addBwRequsItem(new BwRequirement().appId(bwRequs.get(i).get(0)).marBwDl(bwRequs.get(i).get(1)).marBwUl(bwRequs.get(i).get(2)).mirBwDl(bwRequs.get(i).get(3)).mirBwUl(bwRequs.get(i).get(4)));
			}
		}
		if(excepRequs.size()>0) {
			for(int i=0;i<excepRequs.size();i++) {
				if(excepRequs.get(i).get(0)!=null&&excepRequs.get(i).get(1)!=null&&excepRequs.get(i).get(2)!=null) {
					eventSub.addExcepRequsItem(new Exception().excepId(new ExceptionId().excepId(ExceptionIdEnum.fromValue(excepRequs.get(i).get(0))))
															.excepLevel(Integer.parseInt(excepRequs.get(i).get(1)))
															.excepTrend(new ExceptionTrend().excepTrend(ExceptionTrendEnum.fromValue(excepRequs.get(i).get(2)))));
				}
			}
		}
		if(ratFreqs.size()>0) {
			for(int i=0;i<ratFreqs.size();i++) {
				RatFreqInformation ratFreq = new RatFreqInformation();
				if(ratFreqs.get(i).get(0).get(0)!=null) {
					ratFreq.allFreq(ratFreqs.get(i).get(0).get(0).equalsIgnoreCase("TRUE"));
				}
				if(ratFreqs.get(i).get(0).get(1)!=null) {
					ratFreq.allRat(ratFreqs.get(i).get(0).get(1).equalsIgnoreCase("TRUE"));
				}
				if(ratFreqs.get(i).get(0).get(2)!=null) {
					ratFreq.freq(Integer.parseInt(ratFreqs.get(i).get(0).get(2)));
				}
				if(ratFreqs.get(i).get(0).get(3)!=null) {
					ratFreq.ratType(new RatType().ratType(RatTypeEnum.fromValue(ratFreqs.get(i).get(0).get(3))));
				}
				if(ratFreqs.get(i).get(0).get(4)!=null) {
					ratFreq.matchingDir(new MatchingDirection().matchingDir(MatchingDirectionEnum.fromValue(ratFreqs.get(i).get(0).get(4))));
				}
				if(ratFreqs.get(i).get(1).size()>0) {
					ratFreq.svcExpThreshold(new ThresholdLevel().congLevel(Integer.parseInt(ratFreqs.get(i).get(1).get(0))).nfLoadLevel(Integer.parseInt(ratFreqs.get(i).get(1).get(1))).nfCpuUsage(Integer.parseInt(ratFreqs.get(i).get(1).get(2))).nfMemoryUsage(Integer.parseInt(ratFreqs.get(i).get(1).get(3))).nfStorageUsage(Integer.parseInt(ratFreqs.get(i).get(1).get(4))).avgTrafficRate(ratFreqs.get(i).get(1).get(5)).maxTrafficRate(ratFreqs.get(i).get(1).get(6)).avgPacketDelay(Integer.parseInt(ratFreqs.get(i).get(1).get(7))).maxPacketDelay(Integer.parseInt(ratFreqs.get(i).get(1).get(8))).avgPacketLossRate(Integer.parseInt(ratFreqs.get(i).get(1).get(9))).svcExpLevel(Float.parseFloat(ratFreqs.get(i).get(1).get(10))));
				}
				eventSub.addRatFreqsItem(ratFreq);
			}
		}
		if(listOfAnaSubsets.size()>0) {
			for(int i=0;i<listOfAnaSubsets.size();i++) {
				eventSub.addListOfAnaSubsetsItem(new AnalyticsSubset().anaSubset(AnalyticsSubsetEnum.fromValue(listOfAnaSubsets.get(i))));
			}
		}
		if(disperReqs.size()>0) {
			for(int n=0;n<disperReqs.size();n++) {
				DispersionRequirement disperReq = new DispersionRequirement();
				if(disperReqs.get(n).get(0).get(0).get(0)!=null) {
					disperReq.disperType(new DispersionType().disperType(DispersionTypeEnum.fromValue(disperReqs.get(n).get(0).get(0).get(0))));
				}
				if(disperReqs.get(n).get(0).get(0).get(1)!=null) {
					disperReq.dispOrderCriter(new DispersionOrderingCriterion().dispOrderCriter(DispersionOrderingCriterionEnum.fromValue(disperReqs.get(n).get(0).get(0).get(1))));
				}
				if(disperReqs.get(n).get(0).get(0).get(2)!=null) {
					disperReq.order(new MatchingDirection().matchingDir(MatchingDirectionEnum.fromValue(disperReqs.get(n).get(0).get(0).get(2))));
				}
				for(int i=0;i<disperReqs.get(n).get(1).size();i++) {
					disperReq.addClassCritersItem(new ClassCriterion().disperClass(new DispersionClass().disperClass(DispersionClassEnum.fromValue(disperReqs.get(n).get(1).get(i).get(0))))
																	.classThreshold(Integer.parseInt(disperReqs.get(n).get(1).get(i).get(1)))
																	.thresMatch(new MatchingDirection().matchingDir(MatchingDirectionEnum.fromValue(disperReqs.get(n).get(1).get(i).get(2)))));
				}
				for(int i=0;i<disperReqs.get(n).get(2).size();i++) {
					disperReq.addRankCritersItem(new RankingCriterion().highBase(Integer.parseInt(disperReqs.get(n).get(2).get(i).get(0)))
																	.lowBase(Integer.parseInt(disperReqs.get(n).get(2).get(i).get(1))));
				}
				eventSub.addDisperReqsItem(disperReq);
			}
		}
		if(redTransReqs.size()>0) {
			for(int  i=0;i<redTransReqs.size();i++) {
				if(redTransReqs.get(i).get(0)!=null&&redTransReqs.get(i).get(1)!=null) {
					eventSub.addRedTransReqsItem(new RedundantTransmissionExpReq().redTOrderCriter(new RedTransExpOrderingCriterion().redTOrderCriter(RedTransExpOrderingCriterionEnum.fromValue(redTransReqs.get(i).get(0))))
							.order(new MatchingDirection().matchingDir(MatchingDirectionEnum.fromValue(redTransReqs.get(i).get(1)))));
				}
			}
		}
		if(wlanReqs.size()>0) {
			for(int n=0;n<wlanReqs.size();n++) {
				WlanPerformanceReq wlanReq = new WlanPerformanceReq();
				if(wlanReqs.get(n).get(0).get(0)!=null) {
					wlanReq.wlanOrderCriter(new WlanOrderingCriterion().wlanOrderCriter(WlanOrderingCriterionEnum.fromValue(wlanReqs.get(n).get(0).get(0))));
				}
				if(wlanReqs.get(n).get(0).get(1)!=null) {
					wlanReq.order(new MatchingDirection().matchingDir(MatchingDirectionEnum.fromValue(wlanReqs.get(n).get(0).get(1))));
				}
				wlanReq.ssIds(wlanReqs.get(n).get(1));
				wlanReq.bssIds(wlanReqs.get(n).get(2));
				eventSub.addWlanReqsItem(wlanReq);
			}
		}
		if(appServerAddrs.size()>0) {
			for(int i=0;i<appServerAddrs.size();i++) {
				eventSub.addAppServerAddrsItem(new AddrFqdn().ipAddr(new IpAddr().ipv4Addr(appServerAddrs.get(i).get(0)).ipv6Addr(appServerAddrs.get(i).get(1)).ipv6Prefix(appServerAddrs.get(i).get(2)))
															.fqdn(appServerAddrs.get(i).get(3)));
			}
		}
		if(dnPerfReqs.size()>0) {
			for(int n=0;n<dnPerfReqs.size();n++) {
				DnPerformanceReq dnPerfReq = new DnPerformanceReq();
				if(dnPerfReqs.get(n).get(0).get(0).get(0)!=null) {
					dnPerfReq.dnPerfOrderCriter(new DnPerfOrderingCriterion().dnPerfOrderCriter(DnPerfOrderingCriterionEnum.fromValue(dnPerfReqs.get(n).get(0).get(0).get(0))));
				}
				if(dnPerfReqs.get(n).get(0).get(0).get(1)!=null) {
					dnPerfReq.order(new MatchingDirection().matchingDir(MatchingDirectionEnum.fromValue(dnPerfReqs.get(n).get(0).get(0).get(1))));
				}
				for(int i=0;i<dnPerfReqs.get(n).get(1).size();i++) {
					dnPerfReq.addReportThresholdsItem(new ThresholdLevel().congLevel(Integer.parseInt(ratFreqs.get(i).get(1).get(0))).nfLoadLevel(Integer.parseInt(ratFreqs.get(i).get(1).get(1))).nfCpuUsage(Integer.parseInt(ratFreqs.get(i).get(1).get(2))).nfMemoryUsage(Integer.parseInt(ratFreqs.get(i).get(1).get(3))).nfStorageUsage(Integer.parseInt(ratFreqs.get(i).get(1).get(4))).avgTrafficRate(ratFreqs.get(i).get(1).get(5)).maxTrafficRate(ratFreqs.get(i).get(1).get(6)).avgPacketDelay(Integer.parseInt(ratFreqs.get(i).get(1).get(7))).maxPacketDelay(Integer.parseInt(ratFreqs.get(i).get(1).get(8))).avgPacketLossRate(Integer.parseInt(ratFreqs.get(i).get(1).get(9))).svcExpLevel(Float.parseFloat(ratFreqs.get(i).get(1).get(10))));
				}
				eventSub.addDnPerfReqsItem(dnPerfReq);
			}
		}
		if(networkArea.size()>0) {
			NetworkAreaInfo area = new NetworkAreaInfo();
			for(int i=0;i<networkArea.get(0).size();i++) {
				Ecgi ecgi = new Ecgi();
				if(networkArea.get(0).get(i).get(0)!=null&&networkArea.get(0).get(i).get(1)!=null) {
					ecgi.plmnId(new PlmnId().mcc(networkArea.get(0).get(i).get(0)).mnc(networkArea.get(0).get(i).get(1)));
				}
				ecgi.eutraCellId(networkArea.get(0).get(i).get(2)).nid(networkArea.get(0).get(i).get(3));
				area.addEcgisItem(ecgi);
			}
			
			for(int i=0;i<networkArea.get(1).size();i++) {
				Ncgi ncgi = new Ncgi();
				if(networkArea.get(1).get(i).get(1)!=null&&networkArea.get(1).get(i).get(1)!=null) {
					ncgi.plmnId(new PlmnId().mcc(networkArea.get(1).get(i).get(0)).mnc(networkArea.get(1).get(i).get(1)));
				}
				ncgi.nrCellId(networkArea.get(1).get(i).get(2)).nid(networkArea.get(1).get(i).get(3));
				area.addNcgisItem(ncgi);
			}
			
			for(int i=0;i<networkArea.get(2).size();i++) {
				GlobalRanNodeId gRanNodeId = new GlobalRanNodeId();
				if(networkArea.get(2).get(i).get(1)!=null&&networkArea.get(2).get(i).get(1)!=null) {
					gRanNodeId.plmnId(new PlmnId().mcc(networkArea.get(2).get(i).get(0)).mnc(networkArea.get(2).get(i).get(1)));
				}
				gRanNodeId.n3IwfId(networkArea.get(2).get(i).get(2)).ngeNbId(networkArea.get(2).get(i).get(3))
																	.wagfId(networkArea.get(2).get(i).get(4))
																	.tngfId(networkArea.get(2).get(i).get(5))
																	.nid(networkArea.get(2).get(i).get(6))
																	.eNbId(networkArea.get(2).get(i).get(7));
				if(networkArea.get(2).get(i).get(8)!=null&&networkArea.get(2).get(i).get(9)!=null) {
					gRanNodeId.gNbId(new GNbId().bitLength(Integer.parseInt(networkArea.get(2).get(i).get(8))).gNBValue(networkArea.get(2).get(i).get(9)));
				}
				area.addGRanNodeIdsItem(gRanNodeId);
			}
			
			for(int i=0;i<networkArea.get(3).size();i++) {
				Tai tai = new Tai();
				if(networkArea.get(3).get(i).get(1)!=null&&networkArea.get(3).get(i).get(1)!=null) {
					tai.plmnId(new PlmnId().mcc(networkArea.get(3).get(i).get(0)).mnc(networkArea.get(3).get(i).get(1)));
				}
				tai.tac(networkArea.get(3).get(i).get(2)).nid(networkArea.get(3).get(i).get(3));
				area.addTaisItem(tai);
			}
			eventSub.networkArea(area);
		}
		if(qosRequ.size()>0) {
			QosRequirement qosR = new QosRequirement();
			if(qosRequ.get(0)!=null) {
				qosR._5qi(Integer.parseInt(qosRequ.get(0)));
			}
			qosR.gfbrUl(qosRequ.get(1)).gfbrDl(qosRequ.get(2)).per(qosRequ.get(5));
			if(qosRequ.get(3)!=null) {
				qosR.resType(new QosResourceType().resType(QosResourceTypeEnum.fromValue(qosRequ.get(3))));
			}
			if(qosRequ.get(4)!=null) {
				qosR.pdb(Integer.parseInt(qosRequ.get(4)));
			}
		}
		if(exptUeBehav.size()>0) {
			ExpectedUeBehaviourData expUe = new ExpectedUeBehaviourData();
			if(exptUeBehav.get(0).get(0).get(0).get(0).get(0).get(0)!=null) {
				expUe.stationaryIndication(new StationaryIndication().stationaryIndication(StationaryIndicationEnum.fromValue(exptUeBehav.get(0).get(0).get(0).get(0).get(0).get(0))));
			}
			if(exptUeBehav.get(0).get(0).get(0).get(0).get(0).get(1)!=null) {
				expUe.communicationDurationTime(Integer.parseInt(exptUeBehav.get(0).get(0).get(0).get(0).get(0).get(1)));
			}
			if(exptUeBehav.get(0).get(0).get(0).get(0).get(0).get(2)!=null) {
				expUe.periodicTime(Integer.parseInt(exptUeBehav.get(0).get(0).get(0).get(0).get(0).get(2)));
			}
			if(exptUeBehav.get(0).get(0).get(0).get(0).get(0).get(3)!=null) {
				expUe.scheduledCommunicationType(new ScheduledCommunicationType().scheduledCommunicationType(ScheduledCommunicationTypeEnum.fromValue(exptUeBehav.get(0).get(0).get(0).get(0).get(0).get(3))));
			}
			if(exptUeBehav.get(0).get(0).get(0).get(0).get(0).get(4)!=null) {
				expUe.trafficProfile(new TrafficProfile().trafficProfile(TrafficProfileEnum.fromValue(exptUeBehav.get(0).get(0).get(0).get(0).get(0).get(4))));
			}
			if(exptUeBehav.get(0).get(0).get(0).get(0).get(0).get(5)!=null) {
				expUe.validityTime(OffsetDateTime.parse(exptUeBehav.get(0).get(0).get(0).get(0).get(0).get(5)));
			}
			ScheduledCommunicationTime1 schTime = new ScheduledCommunicationTime1();
			if(exptUeBehav.get(1).get(0).get(0).get(0).get(0).size()>0) {
				for(int i=0;i<exptUeBehav.get(1).get(0).get(0).get(0).get(0).size();i++) {
				schTime.addDaysOfWeekItem(Integer.parseInt(exptUeBehav.get(1).get(0).get(0).get(0).get(0).get(i)));
				}
			}
		    schTime.timeOfDayStart(exptUeBehav.get(1).get(0).get(0).get(0).get(1).get(0));
		    schTime.timeOfDayEnd(exptUeBehav.get(1).get(0).get(0).get(0).get(1).get(1));
		    expUe.scheduledCommunicationTime(schTime);
		    BatteryIndication bttrInd = new BatteryIndication();
		    if(exptUeBehav.get(2).get(0).get(0).get(0).get(0).get(0)!=null) {
		    	bttrInd.batteryInd(exptUeBehav.get(2).get(0).get(0).get(0).get(0).get(0).equalsIgnoreCase("TRUE"));
		    }
		    if(exptUeBehav.get(2).get(0).get(0).get(0).get(0).get(1)!=null) {
		    	bttrInd.replaceableInd(exptUeBehav.get(2).get(0).get(0).get(0).get(0).get(1).equalsIgnoreCase("TRUE"));
		    }
		    if(exptUeBehav.get(2).get(0).get(0).get(0).get(0).get(2)!=null) {
		    	bttrInd.rechargeableInd(exptUeBehav.get(2).get(0).get(0).get(0).get(0).get(2).equalsIgnoreCase("TRUE"));
		    }
		    expUe.batteryIndication(bttrInd);
		    if(exptUeBehav.get(3).size()>0) {
		    	for(int n=0;n<exptUeBehav.get(3).size();n++) {
		    		LocationArea area = new LocationArea();
		    		if(exptUeBehav.get(3).get(n).get(0).size()>0) {
		    			NetworkAreaInfo1 netArea = new NetworkAreaInfo1();
		    			for(int i=0;i<exptUeBehav.get(3).get(n).get(0).get(0).size();i++) {
		    				Ecgi ecgi = new Ecgi();
		    				if(exptUeBehav.get(3).get(n).get(0).get(0).get(i).get(0)!=null&&exptUeBehav.get(3).get(n).get(0).get(0).get(i).get(1)!=null) {
		    					ecgi.plmnId(new PlmnId().mcc(exptUeBehav.get(3).get(n).get(0).get(0).get(i).get(0)).mnc(exptUeBehav.get(3).get(n).get(0).get(0).get(i).get(1)));
		    				}
		    				ecgi.eutraCellId(exptUeBehav.get(3).get(n).get(0).get(0).get(i).get(2)).nid(exptUeBehav.get(3).get(n).get(0).get(0).get(i).get(3));
		    				netArea.addEcgisItem(ecgi);
		    			}
		    			
		    			for(int i=0;i<exptUeBehav.get(3).get(n).get(0).get(1).size();i++) {
		    				Ncgi ncgi = new Ncgi();
		    				if(exptUeBehav.get(3).get(n).get(0).get(1).get(i).get(1)!=null&&exptUeBehav.get(3).get(n).get(0).get(1).get(i).get(1)!=null) {
		    					ncgi.plmnId(new PlmnId().mcc(exptUeBehav.get(3).get(n).get(0).get(1).get(i).get(0)).mnc(exptUeBehav.get(3).get(n).get(0).get(1).get(i).get(1)));
		    				}
		    				ncgi.nrCellId(exptUeBehav.get(3).get(n).get(0).get(1).get(i).get(2)).nid(exptUeBehav.get(3).get(n).get(0).get(1).get(i).get(3));
		    				netArea.addNcgisItem(ncgi);
		    			}
		    			
		    			for(int i=0;i<exptUeBehav.get(3).get(n).get(0).get(2).size();i++) {
		    				GlobalRanNodeId gRanNodeId = new GlobalRanNodeId();
		    				if(exptUeBehav.get(3).get(n).get(0).get(2).get(i).get(1)!=null&&exptUeBehav.get(3).get(n).get(0).get(2).get(i).get(1)!=null) {
		    					gRanNodeId.plmnId(new PlmnId().mcc(exptUeBehav.get(3).get(n).get(0).get(2).get(i).get(0)).mnc(exptUeBehav.get(3).get(n).get(0).get(2).get(i).get(1)));
		    				}
		    				gRanNodeId.n3IwfId(exptUeBehav.get(3).get(n).get(0).get(2).get(i).get(2)).ngeNbId(exptUeBehav.get(3).get(n).get(0).get(2).get(i).get(3))
		    																	.wagfId(exptUeBehav.get(3).get(n).get(0).get(2).get(i).get(4))
		    																	.tngfId(exptUeBehav.get(3).get(n).get(0).get(2).get(i).get(5))
		    																	.nid(exptUeBehav.get(3).get(n).get(0).get(2).get(i).get(6))
		    																	.eNbId(exptUeBehav.get(3).get(n).get(0).get(2).get(i).get(7));
		    				if(exptUeBehav.get(3).get(n).get(0).get(2).get(i).get(8)!=null&&exptUeBehav.get(3).get(n).get(0).get(2).get(i).get(9)!=null) {
		    					gRanNodeId.gNbId(new GNbId().bitLength(Integer.parseInt(exptUeBehav.get(3).get(n).get(0).get(2).get(i).get(8))).gNBValue(exptUeBehav.get(3).get(n).get(0).get(2).get(i).get(9)));
		    				}
		    				netArea.addGRanNodeIdsItem(gRanNodeId);
		    			}
		    			
		    			for(int i=0;i<exptUeBehav.get(3).get(n).get(0).get(3).size();i++) {
		    				Tai tai = new Tai();
		    				if(exptUeBehav.get(3).get(n).get(0).get(3).get(i).get(1)!=null&&exptUeBehav.get(3).get(n).get(0).get(3).get(i).get(1)!=null) {
		    					tai.plmnId(new PlmnId().mcc(exptUeBehav.get(3).get(n).get(0).get(3).get(i).get(0)).mnc(exptUeBehav.get(3).get(n).get(0).get(3).get(i).get(1)));
		    				}
		    				tai.tac(exptUeBehav.get(3).get(n).get(0).get(3).get(i).get(2)).nid(exptUeBehav.get(3).get(n).get(0).get(3).get(i).get(3));
		    				netArea.addTaisItem(tai);
		    			}
		    			area.nwAreaInfo(netArea);
		    		}
		    		if(exptUeBehav.get(3).get(n).get(1).size()>0) {
		    			UmtTime t = new UmtTime();
		    			t.timeOfDay(exptUeBehav.get(3).get(n).get(1).get(0).get(0).get(0));
		    			if(exptUeBehav.get(3).get(n).get(1).get(0).get(0).get(1)!=null) {
		    				t.dayOfWeek(Integer.parseInt(exptUeBehav.get(3).get(n).get(1).get(0).get(0).get(1)));
		    			}
		    			area.umtTime(t);
		    		}
		    		if(exptUeBehav.get(3).get(n).get(2).size()>0) {
		    			for(int i=0;i<exptUeBehav.get(3).get(n).get(2).size();i++) {
		    				if(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(0).equalsIgnoreCase("POINT")) {
		    					Point p=new Point();
				    			p.shape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.fromValue("POINT")));
				    			p.point(new GeographicalCoordinates().lon(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(1)))
				    												.lat(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(2))));
				    			area.addGeographicAreasItem(p);
		    				}
		    				if(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(0).equalsIgnoreCase("PointUncertaintyCircle")) {
		    					PointUncertaintyCircle p=new PointUncertaintyCircle();
				    			p.shape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.fromValue("POINT_UNCERTAINTY_CIRCLE")));
				    			p.point(new GeographicalCoordinates().lon(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(1)))
				    												.lat(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(2))));
				    			p.uncertainty(Float.parseFloat(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(3)));
				    			area.addGeographicAreasItem(p);
		    				}
		    				if(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(0).equalsIgnoreCase("PointUncertaintyEllipse")) {
		    					PointUncertaintyEllipse p=new PointUncertaintyEllipse();
				    			p.shape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.fromValue("POINT_UNCERTAINTY_ELLIPSE")));
				    			p.point(new GeographicalCoordinates().lon(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(1)))
				    												.lat(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(2))));
				    			p.confidence(Integer.parseInt(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(3)));
				    			p.uncertaintyEllipse(new UncertaintyEllipse().orientationMajor(Integer.parseInt(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(4)))
				    														.semiMajor(Float.parseFloat(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(4)))
				    														.semiMinor(Float.parseFloat(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(5))));
				    			area.addGeographicAreasItem(p);
		    				}
		    				if(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(0).equalsIgnoreCase("Polygon")) {
		    					Polygon p = new Polygon();
		    					p.shape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.fromValue("POLYGON")));
		    					PointList pl = new PointList();
		    					for(int j=0;j<exptUeBehav.get(3).get(n).get(2).get(i).get(1).size();j++) {
		    						pl.add(new GeographicalCoordinates().lon(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(1).get(j)))
		    															.lat(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(2).get(j))));
		    					}
		    					p.pointList(pl);
		    					area.addGeographicAreasItem(p);
		    				}
		    				if(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(0).equalsIgnoreCase("PointAltitude")) {
		    					PointAltitude p=new PointAltitude();
				    			p.shape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.fromValue("POINT_ALTITUDE")));
				    			p.point(new GeographicalCoordinates().lon(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(1)))
				    												.lat(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(2))));
				    			p.altitude(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(3)));
				    			area.addGeographicAreasItem(p);
		    				}
		    				if(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(0).equalsIgnoreCase("PointAltitudeUncertainty")) {
		    					PointAltitudeUncertainty p=new PointAltitudeUncertainty();
				    			p.shape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.fromValue("POINT_UNCERTAINTY_ELLIPSE")));
				    			p.point(new GeographicalCoordinates().lon(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(1)))
				    												.lat(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(2))));
				    			p.confidence(Integer.parseInt(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(3)));
				    			p.uncertaintyEllipse(new UncertaintyEllipse().orientationMajor(Integer.parseInt(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(4)))
				    														.semiMajor(Float.parseFloat(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(4)))
				    														.semiMinor(Float.parseFloat(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(5))));
				    			p.altitude(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(6)));
				    			p.uncertaintyAltitude(Float.parseFloat(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(7)));
				    			area.addGeographicAreasItem(p);
		    				}
		    				if(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(0).equalsIgnoreCase("EllipsoidArc")) {
		    					EllipsoidArc p=new EllipsoidArc();
				    			p.shape(new SupportedGADShapes().supportedGADShapes(SupportedGADShapesEnum.fromValue("POINT_UNCERTAINTY_ELLIPSE")));
				    			p.point(new GeographicalCoordinates().lon(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(1)))
				    												.lat(Double.parseDouble(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(2))));
				    			p.confidence(Integer.parseInt(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(3)));
				    			p.innerRadius(Integer.parseInt(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(4)));
				    			p.uncertaintyRadius(Float.parseFloat(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(5)));
				    			p.offsetAngle(Integer.parseInt(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(6)));
				    			p.includedAngle(Integer.parseInt(exptUeBehav.get(3).get(n).get(2).get(i).get(0).get(7)));
				    			area.addGeographicAreasItem(p);
		    				}
		    				
		    			}
		    			
		    		}
		    		if(exptUeBehav.get(3).get(n).get(3).get(0).size()>0) {
		    			for(int i=0;i<exptUeBehav.get(3).get(n).get(3).get(0).size();i++) {
			    			CivicAddress civicadd = new CivicAddress();
			    			civicadd.country(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(0))
			    			.a1(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(1))
			    			.a2(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(2))
			    			.a3(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(3))
			    			.a4(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(4))
			    			.a5(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(5))
			    			.a6(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(6))
			    			.PRD(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(7))
			    			.POD(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(8))
			    			.STS(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(9))
			    			.HNO(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(10))
			    			.HNS(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(11))
			    			.LMK(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(12))
			    			.LOC(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(13))
			    			.NAM(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(14))
			    			.PC(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(15))
			    			.BLD(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(16))
			    			.UNIT(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(17))
			    			.FLR(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(18))
			    			.ROOM(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(19))
			    			.PLC(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(20))
			    			.PCN(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(21))
			    			.POD(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(22))
			    			.ADDCODE(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(23))
			    			.SEAT(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(24))
			    			.RD(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(25))
			    			.RDSEC(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(26))
			    			.RDBR(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(27))
			    			.RD(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(28))
			    			.PRM(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(29))
			    			.POM(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(30))
			    			.usageRules(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(31))
			    			.method(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(32))
			    			.providedBy(exptUeBehav.get(3).get(n).get(3).get(0).get(i).get(33));
			    			area.addCivicAddressesItem(civicadd);
		    			}
		    		}
		    		expUe.addExpectedUmtsItem(area);
		    	}
		    }
		    eventSub.exptUeBehav(expUe);
		}
		if(upfInfo.size()>0) {
			UpfInformation upfInf = new UpfInformation();
			upfInf.upfId(upfInfo.get(0));
			if(upfInfo.get(1)!=null||upfInfo.get(2)!=null||upfInfo.get(4)!=null) {
				upfInf.upfAddr(new AddrFqdn().ipAddr(new IpAddr().ipv4Addr(upfInfo.get(1)).ipv6Addr(upfInfo.get(2)).ipv6Prefix(upfInfo.get(3))).fqdn(upfInfo.get(4)));
			}
		}
		if(args.get(1)!=null) {
			eventSub.anySlice(args.get(1).equalsIgnoreCase("TRUE"));
		}
		if(args.get(2)!=null) {
			eventSub.loadLevelThreshold(Integer.parseInt(args.get(2)));
		}
		if(args.get(3)!=null) {
			eventSub.matchingDir(new MatchingDirection().matchingDir(MatchingDirectionEnum.fromValue(args.get(3))));
		}
		if(args.get(4)!=null) {
			eventSub.maxTopAppUlNbr(Integer.parseInt(args.get(4)));
		}
		if(args.get(5)!=null) {
			eventSub.maxTopAppDlNbr(Integer.parseInt(args.get(5)));
		}
		if(args.get(6)!=null) {
			eventSub.repetitionPeriod(Integer.parseInt(args.get(6)));
		}
		if(args.get(7)!=null) {
			eventSub.exptAnaType(new ExpectedAnalyticsType().exptAnaType(ExpectedAnalyticsTypeEnum.fromValue(args.get(7))));
		}
		
		sub.addEventSubscriptionsItem(eventSub);
		return sub;
	}
	public NnwdafEventsSubscription AddOptionalsToSubscription(NnwdafEventsSubscription sub,List<String> optionals,List<String> partitionCriteria, List<String> nfAnaEvents, List<List<List<String>>> ueAnaEvents, List<List<String>> taiList) {
		ReportingInformation evtReq = new ReportingInformation();
		PrevSubInfo prevSub = new PrevSubInfo();
		ConsumerNfInformation consNfInfo = new ConsumerNfInformation();
		if(optionals.get(0)!=null) {
			evtReq.immRep(optionals.get(0).equalsIgnoreCase("TRUE"));
		}
		if(optionals.get(1)!=null) {
			evtReq.notifMethod(new NotificationMethod().notifMethod(NotificationMethodEnum.fromValue(optionals.get(1))));
		}
		if(optionals.get(2)!=null) {
			evtReq.maxReportNbr(Integer.parseInt(optionals.get(2)));
		}
		if(optionals.get(3)!=null) {
			evtReq.monDur(OffsetDateTime.parse(optionals.get(3)));
		}
		if(optionals.get(4)!=null) {
			evtReq.repPeriod(Integer.parseInt(optionals.get(4)));
		}
		if(optionals.get(5)!=null) {
			evtReq.sampRatio(Integer.parseInt(optionals.get(5)));
		}
		if(optionals.get(6)!=null) {
			evtReq.grpRepTime(Integer.parseInt(optionals.get(6)));
		}
		if(optionals.get(7)!=null) {
			evtReq.notifFlag(new NotificationFlag().notifFlag(NotificationFlagEnum.fromValue(optionals.get(7))));
		}
		if(partitionCriteria.size()>0) {
			for(int i=0;i<partitionCriteria.size();i++) {
				evtReq.addPartitionCriteriaItem(new PartitioningCriteria().partitionCriteria(PartitioningCriteriaEnum.fromValue(partitionCriteria.get(i))));
			}
		}
		if((optionals.get(0)!=null) ||(optionals.get(1)!=null)||(optionals.get(2)!=null) ||(optionals.get(3)!=null) ||(optionals.get(4)!=null) ||(optionals.get(5)!=null) ||(optionals.get(6)!=null) ||(optionals.get(7)!=null)){
			sub.evtReq(evtReq);
		}
		if(optionals.get(8)!=null){
			prevSub.producerId(UUID.fromString(optionals.get(8)));
		}
		if(optionals.get(9)!=null){
			prevSub.producerSetId(optionals.get(9));
		}
		if(optionals.get(10)!=null){
			prevSub.subscriptionId(optionals.get(10));
		}
		if(nfAnaEvents.size()>0) {
			for(int i=0;i<nfAnaEvents.size();i++) {
				prevSub.addNfAnaEventsItem(new NwdafEvent().event(NwdafEventEnum.fromValue(nfAnaEvents.get(i))));
			}
		}
		if(ueAnaEvents.size()>0) {
			for(int i=0;i<ueAnaEvents.size();i++) {
				if(ueAnaEvents.get(i).size()==2) {
				UeAnalyticsContextDescriptor ueAnaEvent = new UeAnalyticsContextDescriptor();
				ueAnaEvent.supi(ueAnaEvents.get(i).get(0).get(0));
				for(int j=0;j<ueAnaEvents.get(i).get(1).size();j++) {
					ueAnaEvent.addAnaTypesItem(new NwdafEvent().event(NwdafEventEnum.fromValue(ueAnaEvents.get(i).get(1).get(j))));
				}
				prevSub.addUeAnaEventsItem(ueAnaEvent);
				}
			}
		}
		if(optionals.get(8)!=null||optionals.get(9)!=null||optionals.get(10)!=null||nfAnaEvents.size()>0||ueAnaEvents.size()>0) {
			sub.prevSub(prevSub);
		}
		if(optionals.get(11)!=null){
			sub.notifCorrId(optionals.get(11));
		}
		if(optionals.get(12)!=null){
			consNfInfo.nfId(UUID.fromString(optionals.get(12)));
		}
		if(optionals.get(13)!=null){
			consNfInfo.nfSetId(optionals.get(13));
		}
		if(taiList.size()>0) {
			for(int i=0;i<taiList.size();i++) {
				consNfInfo.addTaiListItem(new Tai().plmnId(new PlmnId().mcc(taiList.get(i).get(0)).mnc(taiList.get(i).get(1))).tac(taiList.get(i).get(2)).nid(taiList.get(i).get(3)));
			}
		}
		if((optionals.get(12)!=null)||(optionals.get(13)!=null)||taiList.size()>0) {
			sub.consNfInfo(consNfInfo);
		}
		return sub;
	}
	public NnwdafEventsSubscription SubscriptionRequestBuilder(String clientURI,
			List<String> optionals,List<String> partitionCriteria, List<String> nfAnaEvents, List<List<List<String>>> ueAnaEvents, List<List<String>> taiList) throws JsonProcessingException {
		
		NnwdafEventsSubscription bodyObject = InitSubscriptionRequest(clientURI);
		bodyObject = AddOptionalsToSubscription(bodyObject, optionals, partitionCriteria, nfAnaEvents, ueAnaEvents, taiList);
		
		System.out.println("Request body:");
		System.out.println(bodyObject.toString());
		return bodyObject;
	}
}
