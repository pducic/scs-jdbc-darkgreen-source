package demo.config;

import demo.messages.MessagesProvider;
import demo.properties.AppPropertiesProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProvidersConfig {

    @Autowired
    private AppPropertiesProvider appPropertiesProvider;

    @Bean
    public MessagesProvider messageQueueProvider(){
        return new MessagesProvider(
                "[_StreamId] as Id, 'Queue' as [_DataSource], null as SentDateTime, null as MessageId, null as ReadyToArchiveTime, CAST(COALESCE(OwnerChannelId, SMSChannelId) AS INT) AS DirectAccountId, null as ManipulatedMessagePriority,                                                                                                                                                                                       null AS ProcessingLatency, CAST(BillingDatabaseId AS INT) AS BillingDatabaseId,  0 AS PricePerMessageEur,                               0 AS SellingPrice, 0 AS BulkPricePerMessage, 0 AS IWFee,                                                         0 AS PurchasePrice,                                                                                             0 AS Margin, NULL AS DrArrivalTime, NULL AS DrReportingTime,                                                               0 AS Paid, *",
                "[spam].[ReadMessageQueueStream4ServiceByDatabaseId_Decorated]",
                -13,
                1,
                false
        );
    }

    @Bean
    public MessagesProvider messageLogProvider(){
        return new MessagesProvider(
                "[_StreamId] as Id,   'Log' as [_DataSource],         SentDateTime,         MessageId,         ReadyToArchiveTime, CAST(COALESCE(OwnerChannelId, SMSChannelId) AS INT) AS DirectAccountId,         ManipulatedMessagePriority, DATEDIFF(HOUR, SendDateTime, SentDateTime) * CAST(1000 * 60 * 60 AS bigint) + DATEDIFF(MILLISECOND, DATEADD(HOUR, DATEDIFF(HOUR, SendDateTime, SentDateTime), SendDateTime), SentDateTime) AS ProcessingLatency, CAST(BillingDatabaseId AS INT) AS BillingDatabaseId,       PricePerMessageEur, (PricePerMessageEur * SMSCount) AS SellingPrice,      BulkPricePerMessage,      IWFee, ((BulkPricePerMessage + CAST(IWFee AS MONEY)) * SMSCount) AS PurchasePrice, ((PricePerMessageEur * SMSCount) - ((BulkPricePerMessage + CAST(IWFee AS MONEY)) * SMSCount)) AS Margin,         DrArrivalTime,         DrReportingTime, CASE WHEN COALESCE(PricePerMessageEur, 0) > 0 THEN 1 ELSE 0 END AS Paid, *",
                "[spam].[ReadMessageLogStream4ServiceByDatabaseId_Decorated]",
                -13,
                1,
                false
        );
    }

}
