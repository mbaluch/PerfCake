/*
 * Copyright 2010-2013 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.perfcake.reporting;

import java.text.DecimalFormat;

import org.perfcake.reporting.destinations.CsvDestination;
import org.perfcake.reporting.reporters.ATReporter;
import org.perfcake.reporting.reporters.LabelTypes;
import org.perfcake.reporting.reporters.Reporter;
import org.testng.annotations.Test;

public class ATReporterTest extends ReportingTestBase {

   protected static final String DECIMAL_FORMAT_STRING = "0.0";

   protected static final DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT_STRING);

   @Test
   public void averageReporter() throws ReportsException {
      Reporter reporter1 = new ATReporter();
      CsvDestination csvDestination = new CsvDestination();
      csvDestination.setOutputPath(TEST_OUTPUT_DIR);
      csvDestination.setPeriodicity("1.001s");

      reporter1.addDestination(csvDestination);
      reporter1.setProperty("time_window_size", "2");
      reporter1.setProperty("decimal_format", DECIMAL_FORMAT_STRING);
      reporter1.setProperty("label_type", "iteration");

      ReportManager rm = new ReportManager();

      rm.setTags("http, gateway");
      rm.setUniqueId("test");
      rm.loadConfigValues();
      rm.addReporter(reporter1);
      rm.assertUntouchedProperties();
      simulateIterations(rm);

      assertCsvContainsExactly(TEST_OUTPUT_DIR + "/test_" + MeasurementTypes.AI_CURRENT + "_" + LabelTypes.ITERATIONS + ".csv", "Iteration;AT_CURRENT\n" + "2;" + decimalFormat.format(2.0) + "\n3;" + decimalFormat.format(0.5) + "\n6;" + decimalFormat.format(2.0) + "\n10;" + decimalFormat.format(3.5));

      assertCsvContainsExactly(TEST_OUTPUT_DIR + "/test_" + MeasurementTypes.AI_TOTAL + "_" + LabelTypes.ITERATIONS + ".csv", "Iteration;AT_TOTAL\n" + "2;" + decimalFormat.format(2.0) + "\n3;" + decimalFormat.format(1.0) + "\n6;" + decimalFormat.format(1.5) + "\n10;" + decimalFormat.format(2.0));
   }
}
