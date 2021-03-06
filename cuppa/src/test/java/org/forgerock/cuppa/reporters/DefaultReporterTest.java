/*
 * Copyright 2015-2016 ForgeRock AS.
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

package org.forgerock.cuppa.reporters;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.forgerock.cuppa.Cuppa.*;
import static org.forgerock.cuppa.TestCuppaSupport.defineTests;
import static org.forgerock.cuppa.TestCuppaSupport.runTests;

import java.io.ByteArrayOutputStream;

import org.forgerock.cuppa.functions.TestFunction;
import org.forgerock.cuppa.model.TestBlock;
import org.testng.annotations.Test;

public class DefaultReporterTest {
    @Test
    public void reporterShouldLookGreatForPassingTests() {

        //Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Reporter reporter = new DefaultReporter(outputStream);
        TestBlock rootBlock = defineTests(() -> {
            describe("describe", () -> {
                when("when", () -> {
                    it("passing test", TestFunction.identity());
                });
            });
        });

        //When
        runTests(rootBlock, reporter);

        //Then
        String output = new String(outputStream.toByteArray(), UTF_8);
        String[] expectedLines = {
                "",
                "",
                "  describe",
                "    when when",
                "      ✓ passing test",
                "",
                "",
                "  1 passing",
        };
        String expectedOutput = String.join(System.lineSeparator(), expectedLines);
        assertThat(output).startsWith(expectedOutput);
    }

    @Test
    public void reporterShouldLookGreatForFailingTests() {

        //Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Reporter reporter = new DefaultReporter(outputStream);
        TestBlock rootBlock = defineTests(() -> {
            describe("describe", () -> {
                when("when", () -> {
                    it("failing test", () -> {
                        assertThat(false).isTrue();
                    });
                });
            });
        });

        //When
        runTests(rootBlock, reporter);

        //Then
        String output = new String(outputStream.toByteArray(), UTF_8);
        String[] expectedLines = {
                "",
                "",
                "  describe",
                "    when when",
                "      1) failing test",
                "",
                "",
                "  0 passing",
                "  1 failing",
                "",
                "  1) describe when when failing test:",
        };
        String expectedOutput = String.join(System.lineSeparator(), expectedLines);
        assertThat(output).startsWith(expectedOutput);
    }

    @Test
    public void reporterShouldLookGreatForErroringTests() {

        //Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Reporter reporter = new DefaultReporter(outputStream);
        TestBlock rootBlock = defineTests(() -> {
            describe("describe", () -> {
                when("when", () -> {
                    it("erroring test", () -> {
                        throw new IllegalStateException();
                    });
                });
            });
        });

        //When
        runTests(rootBlock, reporter);

        //Then
        String output = new String(outputStream.toByteArray(), UTF_8);
        String[] expectedLines = {
                "",
                "",
                "  describe",
                "    when when",
                "      1) erroring test",
                "",
                "",
                "  0 passing",
                "  1 failing",
                "",
                "  1) describe when when erroring test:",
                "     java.lang.IllegalStateException",
        };
        String expectedOutput = String.join(System.lineSeparator(), expectedLines);
        assertThat(output).startsWith(expectedOutput);
    }

    @Test
    public void reporterShouldLookGreatForPendingTests() {

        //Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Reporter reporter = new DefaultReporter(outputStream);
        TestBlock rootBlock = defineTests(() -> {
            describe("describe", () -> {
                when("when", () -> {
                    it("pending test");
                });
            });
        });

        //When
        runTests(rootBlock, reporter);

        //Then
        String output = new String(outputStream.toByteArray(), UTF_8);
        String[] expectedLines = {
                "",
                "",
                "  describe",
                "    when when",
                "      - pending test",
                "",
                "",
                "  0 passing",
                "  1 pending",
        };
        String expectedOutput = String.join(System.lineSeparator(), expectedLines);
        assertThat(output).startsWith(expectedOutput);
    }

    @Test
    public void reporterShouldLookGreatForSkippedTests() {

        //Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Reporter reporter = new DefaultReporter(outputStream);
        TestBlock rootBlock = defineTests(() -> {
            describe("describe", () -> {
                when("when", () -> {
                    skip().it("skipped test", TestFunction.identity());
                });
            });
        });

        //When
        runTests(rootBlock, reporter);

        //Then
        String output = new String(outputStream.toByteArray(), UTF_8);
        String[] expectedLines = {
                "",
                "",
                "  describe",
                "    when when",
                "      - skipped test",
                "",
                "",
                "  0 passing",
                "  1 pending",
        };
        String expectedOutput = String.join(System.lineSeparator(), expectedLines);
        assertThat(output).startsWith(expectedOutput);
    }

    @Test
    public void reporterShouldLookGreatWithNoTests() {

        //Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Reporter reporter = new DefaultReporter(outputStream);
        TestBlock rootBlock = defineTests(() -> {
            describe("describe", () -> {
                when("when", () -> {
                });
            });
        });

        //When
        runTests(rootBlock, reporter);

        //Then
        String output = new String(outputStream.toByteArray(), UTF_8);
        String[] expectedLines = {
                "",
                "",
                "",
                "",
                "  0 passing",
        };
        String expectedOutput = String.join(System.lineSeparator(), expectedLines);
        assertThat(output).startsWith(expectedOutput);
    }

    @Test
    public void reporterShouldLookGreat() {

        //Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Reporter reporter = new DefaultReporter(outputStream);
        TestBlock rootBlock = defineTests(() -> {
            describe("describe", () -> {
                when("when", () -> {
                    it("passing test", TestFunction.identity());
                    it("failing test", () -> {
                        assertThat(false).isTrue();
                    });
                    it("erroring test", () -> {
                        throw new IllegalStateException();
                    });
                    it("pending test");
                    skip().it("skipped test", TestFunction.identity());
                });
            });
        });

        //When
        runTests(rootBlock, reporter);

        //Then
        String output = new String(outputStream.toByteArray(), UTF_8);
        String[] expectedLines = {
                "",
                "",
                "  describe",
                "    when when",
                "      ✓ passing test",
                "      1) failing test",
                "      2) erroring test",
                "      - pending test",
                "      - skipped test",
                "",
                "",
                "  1 passing",
                "  2 failing",
                "  2 pending",
                "",
                "  1) describe when when failing test:",
        };
        String expectedOutput = String.join(System.lineSeparator(), expectedLines);
        assertThat(output).startsWith(expectedOutput);
    }

    @Test
    public void reporterShouldReportFailingHooks() {

        //Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Reporter reporter = new DefaultReporter(outputStream);
        TestBlock rootBlock = defineTests(() -> {
            describe("describe", () -> {
                when("when", () -> {
                    beforeEach(() -> {
                        throw new RuntimeException();
                    });
                    it("passing test", () -> {
                    });
                });
            });
        });

        //When
        runTests(rootBlock, reporter);

        //Then
        String output = new String(outputStream.toByteArray(), UTF_8);
        String[] expectedLines = {
                "",
                "",
                "  describe",
                "    when when",
                "      1) \"beforeEach\" hook",
                "      - passing test",
                "",
                "",
                "  0 passing",
                "  1 failing",
                "  1 pending",
                "",
                "  1) describe when when \"beforeEach\" hook:",
                "     java.lang.RuntimeException",
        };
        String expectedOutput = String.join(System.lineSeparator(), expectedLines);
        assertThat(output).startsWith(expectedOutput);
    }

    @Test
    public void reporterShouldReportFailingNamedHook() {

        //Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Reporter reporter = new DefaultReporter(outputStream);
        TestBlock rootBlock = defineTests(() -> {
            describe("describe", () -> {
                when("when", () -> {
                    beforeEach("my hook", () -> {
                        throw new RuntimeException();
                    });
                    it("passing test", () -> {
                    });
                });
            });
        });

        //When
        runTests(rootBlock, reporter);

        //Then
        String output = new String(outputStream.toByteArray(), UTF_8);
        String[] expectedLines = {
                "",
                "",
                "  describe",
                "    when when",
                "      1) \"beforeEach\" hook \"my hook\"",
                "      - passing test",
                "",
                "",
                "  0 passing",
                "  1 failing",
                "  1 pending",
                "",
                "  1) describe when when \"beforeEach\" hook \"my hook\":",
                "     java.lang.RuntimeException",
        };
        String expectedOutput = String.join(System.lineSeparator(), expectedLines);
        assertThat(output).startsWith(expectedOutput);
    }

    @Test
    public void reporterShouldReportParentsOfFailingHook() {

        //Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Reporter reporter = new DefaultReporter(outputStream);
        TestBlock rootBlock = defineTests(() -> {
            describe("describe", () -> {
                beforeEach(() -> {
                    throw new RuntimeException();
                });
                when("when", () -> {
                    it("passing test", () -> {
                    });
                });
            });
        });

        //When
        runTests(rootBlock, reporter);

        //Then
        String output = new String(outputStream.toByteArray(), UTF_8);
        String[] expectedLines = {
                "",
                "",
                "  describe",
                "    when when",
                "      1) \"beforeEach\" hook",
                "      - passing test",
                "",
                "",
                "  0 passing",
                "  1 failing",
                "  1 pending",
                "",
                "  1) describe \"beforeEach\" hook:",
                "     java.lang.RuntimeException",
        };
        String expectedOutput = String.join(System.lineSeparator(), expectedLines);
        assertThat(output).startsWith(expectedOutput);
    }
}
