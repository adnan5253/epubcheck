package com.adobe.epubcheck.test;

import com.adobe.epubcheck.messages.MessageId;
import junit.framework.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Test the coverage of reported message ids by all the tests.  This test should run after all the others have completed.
 */
public class message_coverage
{
  @Test
  public void MessageId_Coverage_Test() throws Exception
  {
    File dumpFile = new File("ReportedMessages.txt");
    Assert.assertTrue("MessageId output file is missing: " + dumpFile.getAbsolutePath(), dumpFile.exists());
    HashSet<MessageId> reportedMessageIds = new HashSet<MessageId>();
    BufferedReader reader = new BufferedReader(new FileReader(dumpFile));
    Assert.assertNotNull("Could not read the reported message file.", reader);
    String line;
    while ((line = reader.readLine()) != null)
    {
      line = line.trim();
      if (!line.equals(""))
      {
        reportedMessageIds.add(MessageId.fromString(line));
      }
    }
    reader.close();
    List<MessageId> allMessageList = Arrays.asList(MessageId.values());
    Assert.assertNotNull("Could not read the reported message file.", allMessageList);
    Set<MessageId> allMessages = new HashSet<MessageId>(allMessageList);
    allMessages.removeAll(reportedMessageIds);
    Set<MessageId> expectedMissedCoverage = new HashSet<MessageId>();
    expectedMissedCoverage.add(MessageId.HTM_002); //This message is in a code path that shouldn't be hit
    expectedMissedCoverage.add(MessageId.HTM_011); //This message may never be reported.  Undeclared entities result in a Sax Parser Error and message RSC_005.
    expectedMissedCoverage.add(MessageId.CHK_007); //This message is in a code path that shouldn't be hit, but is here in case there is some other error accessing the file.
    expectedMissedCoverage.add(MessageId.CHK_006); //This message is in a code path that shouldn't ever be hit, but its here in case there is an error parsing the regex result.
    expectedMissedCoverage.add(MessageId.PKG_015); //This is only reported in an exception that is difficult to generate in a test
    expectedMissedCoverage.add(MessageId.PKG_005); //This is only reported in an exception that is difficult to generate in a test
    expectedMissedCoverage.add(MessageId.RSC_017); //This message may never be reported.   Sax Parser Error and message RSC_005, but this covers SAX warnings, which may never happen.

    Assert.assertEquals("Messages not covered by tests", expectedMissedCoverage, allMessages);
  }
}