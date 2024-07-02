/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tinkerpop.gremlin.language.translator;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

@RunWith(Enclosed.class)
public class GremlinTranslatorTest {

    @RunWith(Parameterized.class)
    public static class VariableTest {

        @Parameterized.Parameter(value = 0)
        public String query;

        @Parameterized.Parameter(value = 1)
        public List<String> expectedVariables;

        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {"g.V(l1)", Collections.singletonList("l1")},
                    {"g.V().hasLabel('person').has(x, y).as('a').out('knows').as('b').select('a', 'b')", Arrays.asList("x", "y")},
                    {"g.V(x).map(out(y).count())", Arrays.asList("x", "y")},
            });
        }

        @Test
        public void shouldExtractVariablesFromLanguage() {
            final Translation translation = GremlinTranslator.translate(query, Translator.LANGUAGE);
            assertEquals(expectedVariables.size(), translation.getParameters().size());
            assertThat(translation.getParameters().toArray(), arrayContainingInAnyOrder(expectedVariables.toArray()));
        }

        @Test
        public void shouldExtractVariablesFromDotNet() {
            final Translation translation = GremlinTranslator.translate(query, Translator.DOTNET);
            assertEquals(expectedVariables.size(), translation.getParameters().size());
            assertThat(translation.getParameters().toArray(), arrayContainingInAnyOrder(expectedVariables.toArray()));
        }

        @Test
        public void shouldExtractVariablesFromGo() {
            final Translation translation = GremlinTranslator.translate(query, Translator.GO);
            assertEquals(expectedVariables.size(), translation.getParameters().size());
            assertThat(translation.getParameters().toArray(), arrayContainingInAnyOrder(expectedVariables.toArray()));
        }

        @Test
        public void shouldExtractVariablesFromGroovy() {
            final Translation translation = GremlinTranslator.translate(query, Translator.GROOVY);
            assertEquals(expectedVariables.size(), translation.getParameters().size());
            assertThat(translation.getParameters().toArray(), arrayContainingInAnyOrder(expectedVariables.toArray()));
        }

        @Test
        public void shouldExtractVariablesFromJava() {
            final Translation translation = GremlinTranslator.translate(query, Translator.JAVA);
            assertEquals(expectedVariables.size(), translation.getParameters().size());
            assertThat(translation.getParameters().toArray(), arrayContainingInAnyOrder(expectedVariables.toArray()));
        }

        @Test
        public void shouldExtractVariablesFromJavascript() {
            final Translation translation = GremlinTranslator.translate(query, Translator.JAVASCRIPT);
            assertEquals(expectedVariables.size(), translation.getParameters().size());
            assertThat(translation.getParameters().toArray(), arrayContainingInAnyOrder(expectedVariables.toArray()));
        }

        @Test
        public void shouldExtractVariablesFromPython() {
            final Translation translation = GremlinTranslator.translate(query, Translator.PYTHON);
            assertEquals(expectedVariables.size(), translation.getParameters().size());
            assertThat(translation.getParameters().toArray(), arrayContainingInAnyOrder(expectedVariables.toArray()));
        }
    }

    @RunWith(Parameterized.class)
    public static class TranslationTest {
        private final String query;
        private final String expectedForLang;
        private final String expectedForAnonymized;
        private final String expectedForDotNet;
        private final String expectedForGo;
        private final String expectedForGroovy;
        private final String expectedForJava;
        private final String expectedForJavascript;
        private final String expectedForPython;

        /**
         * Test data where first element is the Gremlin query to translate and the following elements are the expected
         * translations for each language.
         * <ol>
         *     <li>Language</li>
         *     <li>Anonymized</li>
         *     <li>.NET</li>
         *     <li>Go</li>
         *     <li>Groovy</li>
         *     <li>Java</li>
         *     <li>Javascript</li>
         *     <li>Python</li>
         * </ol>
         * If the translation is expected end in error then just set the value to the expected error message.
         */
        @Parameterized.Parameters(name = "{0}")
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                    {"g",
                            null,   // language
                            null,   // anonymized
                            null,   // .NET
                            null,   // Go
                            null,   // Groovy
                            null,   // Java
                            null,   // Javascript
                            null},  // Python
                    {"g.with(\"x\")",
                            null,
                            "g.with(string0)",
                            "g.With(\"x\")",
                            "g.With(\"x\")",
                            null,
                            null,
                            "g.with_(\"x\")",
                            "g.with_('x')"},
                    {"g.with(\"x\n\\\"yz\")",
                            null,
                            "g.with(string0)",
                            "g.With(\"x\n\\\"yz\")",
                            "g.With(\"x\n\\\"yz\")",
                            null,
                            null,
                            "g.with_(\"x\n\\\"yz\")",
                            "g.with_('x\n\\\"yz')"},
                    {"g.with('x', 'xyz')",
                            null,
                            "g.with(string0, string1)",
                            "g.With(\"x\", \"xyz\")",
                            "g.With(\"x\", \"xyz\")",
                            null,
                            "g.with(\"x\", \"xyz\")",
                            "g.with_(\"x\", \"xyz\")",
                            "g.with_('x', 'xyz')"},
                    {"g.with('x','xyz')",
                            "g.with('x', 'xyz')",
                            "g.with(string0, string1)",
                            "g.With(\"x\", \"xyz\")",
                            "g.With(\"x\", \"xyz\")",
                            "g.with('x', 'xyz')",
                            "g.with(\"x\", \"xyz\")",
                            "g.with_(\"x\", \"xyz\")",
                            "g.with_('x', 'xyz')"},
                    {"g.with('x', '')",
                            null,
                            "g.with(string0, string1)",
                            "g.With(\"x\", \"\")",
                            "g.With(\"x\", \"\")",
                            null,
                            "g.with(\"x\", \"\")",
                            "g.with_(\"x\", \"\")",
                            "g.with_('x', '')"},
                    {"g.with('x', '     ')",
                            null,
                            "g.with(string0, string1)",
                            "g.With(\"x\", \"     \")",
                            "g.With(\"x\", \"     \")",
                            null,
                            "g.with(\"x\", \"     \")",
                            "g.with_(\"x\", \"     \")",
                            "g.with_('x', '     ')"},
                    {"g.with('x', 'x')",
                            null,
                            "g.with(string0, string0)",
                            "g.With(\"x\", \"x\")",
                            "g.With(\"x\", \"x\")",
                            null,
                            "g.with(\"x\", \"x\")",
                            "g.with_(\"x\", \"x\")",
                            "g.with_('x', 'x')"},
                    {"g.with('x', null)",
                            null,
                            "g.with(string0, object0)",
                            "g.With(\"x\", null)",
                            "g.With(\"x\", nil)",
                            null,
                            "g.with(\"x\", null)",
                            "g.with_(\"x\", null)",
                            "g.with_('x', None)"},
                    {"g.with('x', NaN)",
                            null,
                            "g.with(string0, number0)",
                            "g.With(\"x\", Double.NaN)",
                            "g.With(\"x\", math.NaN())",
                            null,
                            "g.with(\"x\", Double.NaN)",
                            "g.with_(\"x\", Number.NaN)",
                            "g.with_('x', float('nan'))"},
                    {"g.with('x', Infinity)",
                            null,
                            "g.with(string0, number0)",
                            "g.With(\"x\", Double.PositiveInfinity)",
                            "g.With(\"x\", math.Inf(1))",
                            "g.with('x', Double.POSITIVE_INFINITY)",
                            "g.with(\"x\", Double.POSITIVE_INFINITY)",
                            "g.with_(\"x\", Number.POSITIVE_INFINITY)",
                            "g.with_('x', float('inf'))"},
                    {"g.with('x', -Infinity)",
                            null,
                            "g.with(string0, number0)",
                            "g.With(\"x\", Double.NegativeInfinity)",
                            "g.With(\"x\", math.Inf(-1))",
                            "g.with('x', Double.NEGATIVE_INFINITY)",
                            "g.with(\"x\", Double.NEGATIVE_INFINITY)",
                            "g.with_(\"x\", Number.NEGATIVE_INFINITY)",
                            "g.with_('x', float('-inf'))"},
                    {"g.with('x', 1.0)",
                            null,
                            "g.with(string0, number0)",
                            "g.With(\"x\", 1.0)",
                            "g.With(\"x\", 1.0)",
                            null,
                            "g.with(\"x\", 1.0)",
                            "g.with_(\"x\", 1.0)",
                            "g.with_('x', 1.0)",},
                    {"g.with('x', 1.0D)",
                            "g.with('x', 1.0d)",
                            "g.with(string0, double0)",
                            "g.With(\"x\", 1.0d)",
                            "g.With(\"x\", 1.0)",
                            "g.with('x', 1.0d)",
                            "g.with(\"x\", 1.0d)",
                            "g.with_(\"x\", 1.0)",
                            "g.with_('x', 1.0)"},
                    {"g.with('x', 1.0d)",
                            null,
                            "g.with(string0, double0)",
                            "g.With(\"x\", 1.0d)",
                            "g.With(\"x\", 1.0)",
                            null,
                            "g.with(\"x\", 1.0d)",
                            "g.with_(\"x\", 1.0)",
                            "g.with_('x', 1.0)"},
                    {"g.with('x', -1.0d)",
                            null,
                            "g.with(string0, double0)",
                            "g.With(\"x\", -1.0d)",
                            "g.With(\"x\", -1.0)",
                            null,
                            "g.with(\"x\", -1.0d)",
                            "g.with_(\"x\", -1.0)",
                            "g.with_('x', -1.0)"},
                    {"g.with('x', 1.0F)",
                            "g.with('x', 1.0f)",
                            "g.with(string0, float0)",
                            "g.With(\"x\", 1.0f)",
                            "g.With(\"x\", 1.0)",
                            "g.with('x', 1.0f)",
                            "g.with(\"x\", 1.0f)",
                            "g.with_(\"x\", 1.0)",
                            "g.with_('x', 1.0)"},
                    {"g.with('x', 1.0f)",
                            null,
                            "g.with(string0, float0)",
                            "g.With(\"x\", 1.0f)",
                            "g.With(\"x\", 1.0)",
                            null,
                            "g.with(\"x\", 1.0f)",
                            "g.with_(\"x\", 1.0)",
                            "g.with_('x', 1.0)"},
                    {"g.with('x', -1.0F)",
                            "g.with('x', -1.0f)",
                            "g.with(string0, float0)",
                            "g.With(\"x\", -1.0f)",
                            "g.With(\"x\", -1.0)",
                            "g.with('x', -1.0f)",
                            "g.with(\"x\", -1.0f)",
                            "g.with_(\"x\", -1.0)",
                            "g.with_('x', -1.0)"},
                    {"g.with('x', 1.0m)",
                            null,
                            "g.with(string0, bigdecimal0)",
                            "g.With(\"x\", (decimal) 1.0)",
                            "g.With(\"x\", 1.0)",
                            "g.with('x', 1.0)",
                            "g.with(\"x\", new BigDecimal(\"1.0\"))",
                            "g.with_(\"x\", 1.0)",
                            "g.with_('x', 1.0)"},
                    {"g.with('x', -1.0m)",
                            null,
                            "g.with(string0, bigdecimal0)",
                            "g.With(\"x\", (decimal) -1.0)",
                            "g.With(\"x\", -1.0)",
                            "g.with('x', -1.0)",
                            "g.with(\"x\", new BigDecimal(\"-1.0\"))",
                            "g.with_(\"x\", -1.0)",
                            "g.with_('x', -1.0)"},
                    {"g.with('x', -1.0M)",
                            "g.with('x', -1.0m)",
                            "g.with(string0, bigdecimal0)",
                            "g.With(\"x\", (decimal) -1.0)",
                            "g.With(\"x\", -1.0)",
                            "g.with('x', -1.0)",
                            "g.with(\"x\", new BigDecimal(\"-1.0\"))",
                            "g.with_(\"x\", -1.0)",
                            "g.with_('x', -1.0)"},
                    {"g.with('x', 1b)",
                            null,
                            "g.with(string0, byte0)",
                            "g.With(\"x\", (byte) 1)",
                            "g.With(\"x\", 1)",
                            "g.with('x', (byte)1)",
                            "g.with(\"x\", new Byte(1))",
                            "g.with_(\"x\", 1)",
                            "g.with_('x', 1)"},
                    {"g.with('x', 1B)",
                            "g.with('x', 1b)",
                            "g.with(string0, byte0)",
                            "g.With(\"x\", (byte) 1)",
                            "g.With(\"x\", 1)",
                            "g.with('x', (byte)1)",
                            "g.with(\"x\", new Byte(1))",
                            "g.with_(\"x\", 1)",
                            "g.with_('x', 1)"},
                    {"g.with('x', -1b)",
                            null,
                            "g.with(string0, byte0)",
                            "g.With(\"x\", (byte) -1)",
                            "g.With(\"x\", -1)",
                            "g.with('x', (byte)-1)",
                            "g.with(\"x\", new Byte(-1))",
                            "g.with_(\"x\", -1)",
                            "g.with_('x', -1)"},
                    {"g.with('x', 1s)",
                            null,
                            "g.with(string0, short0)",
                            "g.With(\"x\", (short) 1)",
                            "g.With(\"x\", 1)",
                            "g.with('x', (short)1)",
                            "g.with(\"x\", new Short(1))",
                            "g.with_(\"x\", 1)",
                            "g.with_('x', 1)"},
                    {"g.with('x', -1s)",
                            null,
                            "g.with(string0, short0)",
                            "g.With(\"x\", (short) -1)",
                            "g.With(\"x\", -1)",
                            "g.with('x', (short)-1)",
                            "g.with(\"x\", new Short(-1))",
                            "g.with_(\"x\", -1)",
                            "g.with_('x', -1)"},
                    {"g.with('x', 1S)",
                            "g.with('x', 1s)",
                            "g.with(string0, short0)",
                            "g.With(\"x\", (short) 1)",
                            "g.With(\"x\", 1)",
                            "g.with('x', (short)1)",
                            "g.with(\"x\", new Short(1))",
                            "g.with_(\"x\", 1)",
                            "g.with_('x', 1)"},
                    {"g.with('x', 1i)",
                            null,
                            "g.with(string0, integer0)",
                            "g.With(\"x\", 1)",
                            "g.With(\"x\", 1)",
                            null,
                            "g.with(\"x\", 1)",
                            "g.with_(\"x\", 1)",
                            "g.with_('x', 1)"},
                    {"g.with('x', 1I)",
                            "g.with('x', 1i)",
                            "g.with(string0, integer0)",
                            "g.With(\"x\", 1)",
                            "g.With(\"x\", 1)",
                            "g.with('x', 1i)",
                            "g.with(\"x\", 1)",
                            "g.with_(\"x\", 1)",
                            "g.with_('x', 1)"},
                    {"g.with('x', -1i)",
                            null,
                            "g.with(string0, integer0)",
                            "g.With(\"x\", -1)",
                            "g.With(\"x\", -1)",
                            null,
                            "g.with(\"x\", -1)",
                            "g.with_(\"x\", -1)",
                            "g.with_('x', -1)"},
                    {"g.with('x', 1l)",
                            null,
                            "g.with(string0, long0)",
                            "g.With(\"x\", 1l)",
                            "g.With(\"x\", 1)",
                            null,
                            "g.with(\"x\", 1l)",
                            "g.with_(\"x\", 1)",
                            "g.with_('x', long(1))"},
                    {"g.with('x', 1L)",
                            "g.with('x', 1l)",
                            "g.with(string0, long0)",
                            "g.With(\"x\", 1l)",
                            "g.With(\"x\", 1)",
                            "g.with('x', 1l)",
                            "g.with(\"x\", 1l)",
                            "g.with_(\"x\", 1)",
                            "g.with_('x', long(1))"},
                    {"g.with('x', -1l)",
                            null,
                            "g.with(string0, long0)",
                            "g.With(\"x\", -1l)",
                            "g.With(\"x\", -1)",
                            null,
                            "g.with(\"x\", -1l)",
                            "g.with_(\"x\", -1)",
                            "g.with_('x', long(-1))"},
                    {"g.with('x', 1n)",
                            null,
                            "g.with(string0, biginteger0)",
                            "g.With(\"x\", 1)",
                            "g.With(\"x\", 1)",
                            "g.with('x', 1g)",
                            "g.with(\"x\", new BigInteger(\"1\"))",
                            "g.with_(\"x\", 1)",
                            "g.with_('x', 1)"},
                    {"g.with('x', 1N)",
                            "g.with('x', 1n)",
                            "g.with(string0, biginteger0)",
                            "g.With(\"x\", 1)",
                            "g.With(\"x\", 1)",
                            "g.with('x', 1g)",
                            "g.with(\"x\", new BigInteger(\"1\"))",
                            "g.with_(\"x\", 1)",
                            "g.with_('x', 1)"},
                    {"g.with('x', -1n)",
                            null,
                            "g.with(string0, biginteger0)",
                            "g.With(\"x\", -1)",
                            "g.With(\"x\", -1)",
                            "g.with('x', -1g)",
                            "g.with(\"x\", new BigInteger(\"-1\"))",
                            "g.with_(\"x\", -1)",
                            "g.with_('x', -1)"},
                    {"g.with('x', datetime('2023-08-02T00:00:00Z'))",
                            null,
                            "g.with(string0, date0)",
                            "g.With(\"x\", DateTimeOffset.FromUnixTimeMilliseconds(1690934400000))",
                            "g.With(\"x\", time.UnixMilli(1690934400000))",
                            null,
                            "g.with(\"x\", new Date(1690934400000))",
                            "g.with_(\"x\", new Date(1690934400000))",
                            "g.with_('x', datetime.datetime.utcfromtimestamp(1690934400000 / 1000.0))"},
                    {"g.with('x', [x: 1])",
                            "g.with('x', [x:1])",
                            "g.with(string0, map0)",
                            "g.With(\"x\", new Dictionary<object, object> {{ \"x\", 1 }})",
                            "g.With(\"x\", map[interface{}]interface{}{\"x\": 1 })",
                            "g.with('x', [x:1])",
                            "g.with(\"x\", new LinkedHashMap<Object, Object>() {{ put(\"x\", 1); }})",
                            "g.with_(\"x\", new Map([[\"x\", 1]]))",
                            "g.with_('x', { 'x': 1 })"},
                    {"g.with('x', [x:1, new:2])",
                            null,
                            "g.with(string0, map0)",
                            "g.With(\"x\", new Dictionary<object, object> {{ \"x\", 1 }, { \"new\", 2 }})",
                            "g.With(\"x\", map[interface{}]interface{}{\"x\": 1, \"new\": 2 })",
                            null,
                            "g.with(\"x\", new LinkedHashMap<Object, Object>() {{ put(\"x\", 1); put(\"new\", 2); }})",
                            "g.with_(\"x\", new Map([[\"x\", 1], [\"new\", 2]]))",
                            "g.with_('x', { 'x': 1, 'new': 2 })"},
                    {"g.with('x', [\"x\":1])",
                            null,
                            "g.with(string0, map0)",
                            "g.With(\"x\", new Dictionary<object, object> {{ \"x\", 1 }})",
                            "g.With(\"x\", map[interface{}]interface{}{\"x\": 1 })",
                            null,
                            "g.with(\"x\", new LinkedHashMap<Object, Object>() {{ put(\"x\", 1); }})",
                            "g.with_(\"x\", new Map([[\"x\", 1]]))",
                            "g.with_('x', { 'x': 1 })"},
                    {"g.with('x', [1:'x'])",
                            null,
                            "g.with(string0, map0)",
                            "g.With(\"x\", new Dictionary<object, object> {{ 1, \"x\" }})",
                            "g.With(\"x\", map[interface{}]interface{}{1: \"x\" })",
                            null,
                            "g.with(\"x\", new LinkedHashMap<Object, Object>() {{ put(1, \"x\"); }})",
                            "g.with_(\"x\", new Map([[1, \"x\"]]))",
                            "g.with_('x', { 1: 'x' })"},
                    {"g.with('x', {1, 'x'})",
                            null,
                            "g.with(string0, set0)",
                            "g.With(\"x\", new HashSet<object> { 1, \"x\" })",
                            "g.With(\"x\", gremlingo.NewSimpleSet(1, \"x\"))",
                            null,
                            "g.with(\"x\", new HashSet<Object>() {{ add(1); add(\"x\"); }})",
                            "g.with_(\"x\", new Set([1, \"x\"]))",
                            "g.with_('x', {1, 'x'})"},
                    {"g.with('x', [1, 'x'])",
                            null,
                            "g.with(string0, list0)",
                            "g.With(\"x\", new List<object> { 1, \"x\" })",
                            "g.With(\"x\", []interface{}{1, \"x\"})",
                            null,
                            "g.with(\"x\", new ArrayList<Object>() {{ add(1); add(\"x\"); }})",
                            "g.with_(\"x\", [1, \"x\"])",
                            "g.with_('x', [1, 'x'])"},
                    {"g.with('x', 0..5)",
                            null,
                            "g.with(string0, number0..number1)",
                            ".NET does not support range literals",
                            "Go does not support range literals",
                            "g.with('x', 0..5)",
                            "Java does not support range literals",
                            "Javascript does not support range literals",
                            "Python does not support range literals"},
                    {"g.withBulk(false)",
                            null,
                            "g.withBulk(boolean0)",
                            "g.WithBulk(false)",
                            "g.WithBulk(false)",
                            null,
                            null,
                            null,
                            "g.with_bulk(False)"},
                    {"g.withBulk(true)",
                            null,
                            "g.withBulk(boolean0)",
                            "g.WithBulk(true)",
                            "g.WithBulk(true)",
                            null,
                            null,
                            null,
                            "g.with_bulk(True)"},
                    {"g.withBulk( true )",
                            "g.withBulk(true)",
                            "g.withBulk(boolean0)",
                            "g.WithBulk(true)",
                            "g.WithBulk(true)",
                            "g.withBulk(true)",
                            "g.withBulk(true)",
                            "g.withBulk(true)",
                            "g.with_bulk(True)"},
                    {"g.withBulk(x)",
                            null,
                            null,
                            "g.WithBulk(x)",
                            "g.WithBulk(x)",
                            null,
                            null,
                            null,
                            "g.with_bulk(x)"},
                    {"g.withStrategies(ReadOnlyStrategy)",
                            null,
                            null,
                            "g.WithStrategies(new ReadOnlyStrategy())",
                            "g.WithStrategies(gremlingo.ReadOnlyStrategy())",
                            null,
                            "g.withStrategies(ReadOnlyStrategy.instance())",
                            "g.withStrategies(new ReadOnlyStrategy())",
                            "g.with_strategies(ReadOnlyStrategy())"},
                    {"g.withStrategies(new SeedStrategy(seed:10000))",
                            null,
                            "g.withStrategies(new SeedStrategy(seed:number0))",
                            "g.WithStrategies(new SeedStrategy(seed: 10000))",
                            "g.WithStrategies(gremlingo.SeedStrategy(gremlingo.SeedStrategyConfig{Seed: 10000}))",
                            null,
                            "g.withStrategies(SeedStrategy.build().seed(10000).create())",
                            "g.withStrategies(new SeedStrategy({seed: 10000}))",
                            "g.with_strategies(SeedStrategy(seed=10000))"},
                    {"g.withStrategies(new PartitionStrategy(includeMetaProperties: true, partitionKey:'x'))",
                            "g.withStrategies(new PartitionStrategy(includeMetaProperties:true, partitionKey:'x'))",
                            "g.withStrategies(new PartitionStrategy(includeMetaProperties:boolean0, partitionKey:string0))",
                            "g.WithStrategies(new PartitionStrategy(includeMetaProperties: true, partitionKey: \"x\"))",
                            "g.WithStrategies(gremlingo.PartitionStrategy(gremlingo.PartitionStrategyConfig{IncludeMetaProperties: true, PartitionKey: \"x\"}))",
                            "g.withStrategies(new PartitionStrategy(includeMetaProperties:true, partitionKey:'x'))",
                            "g.withStrategies(PartitionStrategy.build().includeMetaProperties(true).partitionKey(\"x\").create())",
                            "g.withStrategies(new PartitionStrategy({includeMetaProperties: true, partitionKey: \"x\"}))",
                            "g.with_strategies(PartitionStrategy(include_meta_properties=True, partition_key='x'))"},
                    {"g.withStrategies(new SubgraphStrategy(vertices:__.has('name', 'vadas'), edges: has('weight', gt(0.5))))",
                            "g.withStrategies(new SubgraphStrategy(vertices:__.has('name', 'vadas'), edges:__.has('weight', P.gt(0.5))))",
                            "g.withStrategies(new SubgraphStrategy(vertices:__.has(string0, string1), edges:__.has(string2, P.gt(number0))))",
                            "g.WithStrategies(new SubgraphStrategy(vertices: __.Has(\"name\", \"vadas\"), edges: __.Has(\"weight\", P.Gt(0.5))))",
                            "g.WithStrategies(gremlingo.SubgraphStrategy(gremlingo.SubgraphStrategyConfig{Vertices: gremlingo.T__.Has(\"name\", \"vadas\"), Edges: gremlingo.T__.Has(\"weight\", gremlingo.P.Gt(0.5))}))",
                            "g.withStrategies(new SubgraphStrategy(vertices:__.has('name', 'vadas'), edges:__.has('weight', P.gt(0.5))))",
                            "g.withStrategies(SubgraphStrategy.build().vertices(__.has(\"name\", \"vadas\")).edges(__.has(\"weight\", P.gt(0.5))).create())",
                            "g.withStrategies(new SubgraphStrategy({vertices: __.has(\"name\", \"vadas\"), edges: __.has(\"weight\", P.gt(0.5))}))",
                            "g.with_strategies(SubgraphStrategy(vertices=__.has('name', 'vadas'), edges=__.has('weight', P.gt(0.5))))"},
                    {"g.withStrategies(new SubgraphStrategy(checkAdjacentVertices: false,\n" +
                            "                                            vertices: __.has(\"name\", P.within(\"josh\", \"lop\", \"ripple\")),\n" +
                            "                                            edges: __.or(__.has(\"weight\", 0.4).hasLabel(\"created\"),\n" +
                            "                                                         __.has(\"weight\", 1.0).hasLabel(\"created\")))).E()",
                            "g.withStrategies(new SubgraphStrategy(checkAdjacentVertices:false, vertices:__.has(\"name\", P.within(\"josh\", \"lop\", \"ripple\")), edges:__.or(__.has(\"weight\", 0.4).hasLabel(\"created\"), __.has(\"weight\", 1.0).hasLabel(\"created\")))).E()",
                            "g.withStrategies(new SubgraphStrategy(checkAdjacentVertices:boolean0, vertices:__.has(string0, P.within(string1, string2, string3)), edges:__.or(__.has(string4, number0).hasLabel(string5), __.has(string4, number1).hasLabel(string5)))).E()",
                            "g.WithStrategies(new SubgraphStrategy(checkAdjacentVertices: false, vertices: __.Has(\"name\", P.Within(\"josh\", \"lop\", \"ripple\")), edges: __.Or(__.Has(\"weight\", 0.4).HasLabel(\"created\"), __.Has(\"weight\", 1.0).HasLabel(\"created\")))).E()",
                            "g.WithStrategies(gremlingo.SubgraphStrategy(gremlingo.SubgraphStrategyConfig{CheckAdjacentVertices: false, Vertices: gremlingo.T__.Has(\"name\", gremlingo.P.Within(\"josh\", \"lop\", \"ripple\")), Edges: gremlingo.T__.Or(gremlingo.T__.Has(\"weight\", 0.4).HasLabel(\"created\"), gremlingo.T__.Has(\"weight\", 1.0).HasLabel(\"created\"))})).E()",
                            "g.withStrategies(new SubgraphStrategy(checkAdjacentVertices:false, vertices:__.has(\"name\", P.within(\"josh\", \"lop\", \"ripple\")), edges:__.or(__.has(\"weight\", 0.4).hasLabel(\"created\"), __.has(\"weight\", 1.0).hasLabel(\"created\")))).E()",
                            "g.withStrategies(SubgraphStrategy.build().checkAdjacentVertices(false).vertices(__.has(\"name\", P.within(\"josh\", \"lop\", \"ripple\"))).edges(__.or(__.has(\"weight\", 0.4).hasLabel(\"created\"), __.has(\"weight\", 1.0).hasLabel(\"created\"))).create()).E()",
                            "g.withStrategies(new SubgraphStrategy({checkAdjacentVertices: false, vertices: __.has(\"name\", P.within(\"josh\", \"lop\", \"ripple\")), edges: __.or(__.has(\"weight\", 0.4).hasLabel(\"created\"), __.has(\"weight\", 1.0).hasLabel(\"created\"))})).E()",
                            "g.with_strategies(SubgraphStrategy(check_adjacent_vertices=False, vertices=__.has('name', P.within('josh', 'lop', 'ripple')), edges=__.or_(__.has('weight', 0.4).has_label('created'), __.has('weight', 1.0).has_label('created')))).E()"},
                    {"g.withStrategies(new PartitionStrategy(partitionKey: \"_partition\", writePartition: \"a\", readPartitions: [\"a\",\"b\"])).V().values(\"name\")",
                            "g.withStrategies(new PartitionStrategy(partitionKey:\"_partition\", writePartition:\"a\", readPartitions:[\"a\", \"b\"])).V().values(\"name\")",
                            "g.withStrategies(new PartitionStrategy(partitionKey:string0, writePartition:string1, readPartitions:list0)).V().values(string2)",
                            "g.WithStrategies(new PartitionStrategy(partitionKey: \"_partition\", writePartition: \"a\", readPartitions: new HashSet<string> { \"a\", \"b\" })).V().Values<object>(\"name\")",
                            "g.WithStrategies(gremlingo.PartitionStrategy(gremlingo.PartitionStrategyConfig{PartitionKey: \"_partition\", WritePartition: \"a\", ReadPartitions: gremlingo.NewSimpleSet(\"a\", \"b\")})).V().Values(\"name\")",
                            "g.withStrategies(new PartitionStrategy(partitionKey:\"_partition\", writePartition:\"a\", readPartitions:[\"a\", \"b\"])).V().values(\"name\")",
                            "g.withStrategies(PartitionStrategy.build().partitionKey(\"_partition\").writePartition(\"a\").readPartitions(new ArrayList<Object>() {{ add(\"a\"); add(\"b\"); }}).create()).V().values(\"name\")",
                            "g.withStrategies(new PartitionStrategy({partitionKey: \"_partition\", writePartition: \"a\", readPartitions: [\"a\", \"b\"]})).V().values(\"name\")",
                            "g.with_strategies(PartitionStrategy(partition_key='_partition', write_partition='a', read_partitions=['a', 'b'])).V().values('name')"},
                    {"g.withoutStrategies(ReadOnlyStrategy)",
                            null,
                            null,
                            "g.WithoutStrategies(typeof(ReadOnlyStrategy))",
                            "g.WithoutStrategies(ReadOnlyStrategy)", // go - needs TINKERPOP-3055
                            null,
                            "g.withoutStrategies(ReadOnlyStrategy.class)",
                            "g.withoutStrategies(ReadOnlyStrategy)",  // javascript needs TINKERPOP-3055
                            "g.without_strategies(*[GremlinType('org.apache.tinkerpop.gremlin.process.traversal.strategy.verification.ReadOnlyStrategy')])"},
                    {"g.withoutStrategies(ReadOnlyStrategy, PathRetractionStrategy, FilterRankingStrategy)",
                            null,
                            null,
                            "g.WithoutStrategies(typeof(ReadOnlyStrategy), typeof(PathRetractionStrategy), typeof(FilterRankingStrategy))",
                            "g.WithoutStrategies(ReadOnlyStrategy, PathRetractionStrategy, FilterRankingStrategy)", // go - needs TINKERPOP-3055
                            null,
                            "g.withoutStrategies(ReadOnlyStrategy.class, PathRetractionStrategy.class, FilterRankingStrategy.class)",
                            "g.withoutStrategies(ReadOnlyStrategy, PathRetractionStrategy, FilterRankingStrategy)",  // javascript - needs TINKERPOP-3055
                            "g.without_strategies(*[GremlinType('org.apache.tinkerpop.gremlin.process.traversal.strategy.verification.ReadOnlyStrategy'), GremlinType('org.apache.tinkerpop.gremlin.process.traversal.strategy.optimization.PathRetractionStrategy'), GremlinType('org.apache.tinkerpop.gremlin.process.traversal.strategy.optimization.FilterRankingStrategy')])"},
                    {"g.inject(0..5)",
                            null,
                            "g.inject(number0..number1)",
                            ".NET does not support range literals",
                            "Go does not support range literals",
                            "g.inject(0..5)",
                            "Java does not support range literals",
                            "Javascript does not support range literals",
                            "Python does not support range literals"},
                    {"g.inject(1694017707000).asDate()",
                            null,
                            "g.inject(number0).asDate()",
                            "g.Inject<object>(1694017707000).AsDate()",
                            "g.Inject(1694017707000).AsDate()",
                            null,
                            null,
                            null,
                            "g.inject(long(1694017707000)).as_date()"},
                    {"g.inject(null, null).inject(null, null)",
                            null,
                            "g.inject(object0, object0).inject(object0, object0)",
                            "g.Inject<object>(null, null).Inject(null, null)",
                            "g.Inject(nil, nil).Inject(nil, nil)",
                            "g.inject(null, (Object) null).inject(null, (Object) null)",
                            "g.inject(null, null).inject(null, null)",
                            "g.inject(null, null).inject(null, null)",
                            "g.inject(None, None).inject(None, None)"},
                    {"g.V().hasLabel(null)",
                            null,
                            "g.V().hasLabel(string0)",
                            "g.V().HasLabel((string) null)",
                            "g.V().HasLabel(nil)",
                            null,
                            null,
                            null,
                            "g.V().has_label(None)"},
                    {"g.V().hasLabel('person')",
                            null,
                            "g.V().hasLabel(string0)",
                            "g.V().HasLabel(\"person\")",
                            "g.V().HasLabel(\"person\")",
                            "g.V().hasLabel('person')",
                            "g.V().hasLabel(\"person\")",
                            "g.V().hasLabel(\"person\")",
                            "g.V().has_label('person')"},
                    {"g.V().hasLabel('person', 'software', 'class')",
                            null,
                            "g.V().hasLabel(string0, string1, string2)",
                            "g.V().HasLabel(\"person\", \"software\", \"class\")",
                            "g.V().HasLabel(\"person\", \"software\", \"class\")",
                            "g.V().hasLabel('person', 'software', 'class')",
                            "g.V().hasLabel(\"person\", \"software\", \"class\")",
                            "g.V().hasLabel(\"person\", \"software\", \"class\")",
                            "g.V().has_label('person', 'software', 'class')"},
                    {"g.V().hasLabel(null, 'software', 'class')",
                            null,
                            "g.V().hasLabel(string0, string1, string2)",
                            "g.V().HasLabel(null, \"software\", \"class\")",
                            "g.V().HasLabel(nil, \"software\", \"class\")",
                            "g.V().hasLabel(null, 'software', 'class')",
                            "g.V().hasLabel(null, \"software\", \"class\")",
                            "g.V().hasLabel(null, \"software\", \"class\")",
                            "g.V().has_label(None, 'software', 'class')"},
                    {"g.V().map(__.out().count())",
                            null,
                            null,
                            "g.V().Map<object>(__.Out().Count())",
                            "g.V().Map(gremlingo.T__.Out().Count())",
                            null,
                            null,
                            null,
                            null},
                    {"g.V().has(null, null)",
                            null,
                            "g.V().has(string0, object0)",
                            "g.V().Has((string) null, (object) null)",
                            "g.V().Has(nil, nil)",
                            null,
                            null,
                            null,
                            "g.V().has(None, None)"},
                    {"g.V().map(out().count())",
                            "g.V().map(__.out().count())",
                            "g.V().map(__.out().count())",
                            "g.V().Map<object>(__.Out().Count())",
                            "g.V().Map(gremlingo.T__.Out().Count())",
                            "g.V().map(__.out().count())",
                            "g.V().map(__.out().count())",
                            "g.V().map(__.out().count())",
                            "g.V().map(__.out().count())"},
                    {"g.V().fold().count(local)",
                            "g.V().fold().count(Scope.local)",
                            "g.V().fold().count(Scope.local)",
                            "g.V().Fold().Count(Scope.Local)",
                            "g.V().Fold().Count(gremlingo.Scope.Local)",
                            "g.V().fold().count(Scope.local)",
                            "g.V().fold().count(Scope.local)",
                            "g.V().fold().count(Scope.local)",
                            "g.V().fold().count(Scope.local)"},
                    {"g.V().fold().count(Scope.local)",
                            null,
                            null,
                            "g.V().Fold().Count(Scope.Local)",
                            "g.V().Fold().Count(gremlingo.Scope.Local)",
                            null,
                            null,
                            null,
                            null},
                    {"g.V().has(T.id, 1)",
                            null,
                            "g.V().has(T.id, number0)",
                            "g.V().Has(T.Id, 1)",
                            "g.V().Has(gremlingo.T.Id, 1)",
                            null,
                            null,
                            null,
                            "g.V().has(T.id_, 1)"},
                    {"g.V().has(id, 1)",
                            "g.V().has(T.id, 1)",
                            "g.V().has(T.id, number0)",
                            "g.V().Has(T.Id, 1)",
                            "g.V().Has(gremlingo.T.Id, 1)",
                            "g.V().has(T.id, 1)",
                            "g.V().has(T.id, 1)",
                            "g.V().has(T.id, 1)",
                            "g.V().has(T.id_, 1)"},
                    {"g.V().has(\"name\", P.within(\"josh\",\"stephen\"))",
                            "g.V().has(\"name\", P.within(\"josh\", \"stephen\"))",
                            "g.V().has(string0, P.within(string1, string2))",
                            "g.V().Has(\"name\", P.Within(\"josh\", \"stephen\"))",
                            "g.V().Has(\"name\", gremlingo.P.Within(\"josh\", \"stephen\"))",
                            "g.V().has(\"name\", P.within(\"josh\", \"stephen\"))",
                            "g.V().has(\"name\", P.within(\"josh\", \"stephen\"))",
                            "g.V().has(\"name\", P.within(\"josh\", \"stephen\"))",
                            "g.V().has('name', P.within('josh', 'stephen'))"},
                    {"g.V().has(\"name\", P.eq(\"josh\"))",
                            null,
                            "g.V().has(string0, P.eq(string1))",
                            "g.V().Has(\"name\", P.Eq(\"josh\"))",
                            "g.V().Has(\"name\", gremlingo.P.Eq(\"josh\"))",
                            null,
                            null,
                            null,
                            "g.V().has('name', P.eq('josh'))"},
                    {"g.V().has(\"name\", P.eq(\"josh\").negate())",
                            null,
                            "g.V().has(string0, P.eq(string1).negate())",
                            "g.V().Has(\"name\", P.Eq(\"josh\").Negate())",
                            "g.V().Has(\"name\", gremlingo.P.Eq(\"josh\").Negate())",
                            null,
                            null,
                            null,
                            "g.V().has('name', P.eq('josh').negate())"},
                    {"g.V().has(\"name\", P.within())",
                            null,
                            "g.V().has(string0, P.within())",
                            "g.V().Has(\"name\", P.Within())",
                            "g.V().Has(\"name\", gremlingo.P.Within())",
                            null,
                            null,
                            null,
                            "g.V().has('name', P.within())"},
                    {"g.V().has(\"name\", P.within(\"josh\",\"stephen\").or(eq(\"vadas\")))",
                            "g.V().has(\"name\", P.within(\"josh\", \"stephen\").or(P.eq(\"vadas\")))",
                            "g.V().has(string0, P.within(string1, string2).or(P.eq(string3)))",
                            "g.V().Has(\"name\", P.Within(\"josh\", \"stephen\").Or(P.Eq(\"vadas\")))",
                            "g.V().Has(\"name\", gremlingo.P.Within(\"josh\", \"stephen\").Or(gremlingo.P.Eq(\"vadas\")))",
                            "g.V().has(\"name\", P.within(\"josh\", \"stephen\").or(P.eq(\"vadas\")))",
                            "g.V().has(\"name\", P.within(\"josh\", \"stephen\").or(P.eq(\"vadas\")))",
                            "g.V().has(\"name\", P.within(\"josh\", \"stephen\").or(P.eq(\"vadas\")))",
                            "g.V().has('name', P.within('josh', 'stephen').or_(P.eq('vadas')))"},
                    {"g.V().has(\"name\", within(\"josh\", \"stephen\"))",
                            "g.V().has(\"name\", P.within(\"josh\", \"stephen\"))",
                            "g.V().has(string0, P.within(string1, string2))",
                            "g.V().Has(\"name\", P.Within(\"josh\", \"stephen\"))",
                            "g.V().Has(\"name\", gremlingo.P.Within(\"josh\", \"stephen\"))",
                            "g.V().has(\"name\", P.within(\"josh\", \"stephen\"))",
                            "g.V().has(\"name\", P.within(\"josh\", \"stephen\"))",
                            "g.V().has(\"name\", P.within(\"josh\", \"stephen\"))",
                            "g.V().has('name', P.within('josh', 'stephen'))"},
                    {"g.V().has(\"name\", TextP.containing(\"j\").negate())",
                            null,
                            "g.V().has(string0, TextP.containing(string1).negate())",
                            "g.V().Has(\"name\", TextP.Containing(\"j\").Negate())",
                            "g.V().Has(\"name\", gremlingo.TextP.Containing(\"j\").Negate())",
                            null,
                            null,
                            null,
                            "g.V().has('name', TextP.containing('j').negate())"},
                    {"g.V().hasLabel(\"person\").has(\"age\", P.not(P.lte(10).and(P.not(P.between(11, 20)))).and(P.lt(29).or(P.eq(35)))).values(\"name\")",
                            null,
                            "g.V().hasLabel(string0).has(string1, P.not(P.lte(number0).and(P.not(P.between(number1, number2)))).and(P.lt(number3).or(P.eq(number4)))).values(string2)",
                            "g.V().HasLabel(\"person\").Has(\"age\", P.Not(P.Lte(10).And(P.Not(P.Between(11, 20)))).And(P.Lt(29).Or(P.Eq(35)))).Values<object>(\"name\")",
                            "g.V().HasLabel(\"person\").Has(\"age\", gremlingo.P.Not(gremlingo.P.Lte(10).And(gremlingo.P.Not(gremlingo.P.Between(11, 20)))).And(gremlingo.P.Lt(29).Or(gremlingo.P.Eq(35)))).Values(\"name\")",
                            null,
                            null,
                            null,
                            "g.V().has_label('person').has('age', P.not_(P.lte(10).and_(P.not_(P.between(11, 20)))).and_(P.lt(29).or_(P.eq(35)))).values('name')"},
                    {"g.V().has(\"name\", containing(\"j\"))",
                            "g.V().has(\"name\", TextP.containing(\"j\"))",
                            "g.V().has(string0, TextP.containing(string1))",
                            "g.V().Has(\"name\", TextP.Containing(\"j\"))",
                            "g.V().Has(\"name\", gremlingo.TextP.Containing(\"j\"))",
                            "g.V().has(\"name\", TextP.containing(\"j\"))",
                            "g.V().has(\"name\", TextP.containing(\"j\"))",
                            "g.V().has(\"name\", TextP.containing(\"j\"))",
                            "g.V().has('name', TextP.containing('j'))"},
                    {"g.V().property(set, \"name\", \"stephen\")",
                            "g.V().property(Cardinality.set, \"name\", \"stephen\")",
                            "g.V().property(Cardinality.set, string0, string1)",
                            "g.V().Property(Cardinality.Set, \"name\", \"stephen\")",
                            "g.V().Property(gremlingo.Cardinality.Set, \"name\", \"stephen\")",
                            "g.V().property(Cardinality.set, \"name\", \"stephen\")",
                            "g.V().property(Cardinality.set, \"name\", \"stephen\")",
                            "g.V().property(Cardinality.set, \"name\", \"stephen\")",
                            "g.V().property(Cardinality.set_, 'name', 'stephen')"},
                    {"g.V().property(Cardinality.set, \"name\", \"stephen\")",
                            null,
                            "g.V().property(Cardinality.set, string0, string1)",
                            "g.V().Property(Cardinality.Set, \"name\", \"stephen\")",
                            "g.V().Property(gremlingo.Cardinality.Set, \"name\", \"stephen\")",
                            null,
                            null,
                            null,
                            "g.V().property(Cardinality.set_, 'name', 'stephen')"},
                    {"g.V().has('name', 'foo').property([\"name\":Cardinality.set(\"bar\"), \"age\":43])",
                            null,
                            "g.V().has(string0, string1).property(map0)",
                            "g.V().Has(\"name\", \"foo\").Property(new Dictionary<object, object> {{ \"name\", CardinalityValue.Set(\"bar\") }, { \"age\", 43 }})",
                            "g.V().Has(\"name\", \"foo\").Property(map[interface{}]interface{}{\"name\": gremlingo.CardinalityValue.Set(\"bar\"), \"age\": 43 })",
                            null,
                            "g.V().has(\"name\", \"foo\").property(new LinkedHashMap<Object, Object>() {{ put(\"name\", Cardinality.set(\"bar\")); put(\"age\", 43); }})",
                            "g.V().has(\"name\", \"foo\").property(new Map([[\"name\", CardinalityValue.set(\"bar\")], [\"age\", 43]]))",
                            "g.V().has('name', 'foo').property({ 'name': CardinalityValue.set_('bar'), 'age': 43 })"},
                    {"g.V(new Vertex(1, \"person\")).limit(1)",
                            null,
                            "g.V(new Vertex(number0, string0)).limit(number0)",
                            "g.V(new Vertex(1, \"person\")).Limit<object>(1)",
                            "g.V(gremlingo.Vertex{Element{1, \"person\"}}).Limit(1)",
                            "g.V(new ReferenceVertex(1, \"person\")).limit(1)",
                            "g.V(new ReferenceVertex(1, \"person\")).limit(1)",
                            "g.V(new Vertex(1, \"person\")).limit(1)",
                            "g.V(Vertex(1, 'person')).limit(1)",},
                    {"g.V().both().properties().dedup().hasKey(\"age\").value()",
                            null,
                            "g.V().both().properties().dedup().hasKey(string0).value()",
                            "g.V().Both().Properties<object>().Dedup().HasKey(\"age\").Value<object>()",
                            "g.V().Both().Properties().Dedup().HasKey(\"age\").Value()",
                            null,
                            null,
                            null,
                            "g.V().both().properties().dedup().has_key('age').value()",},
                    {"g.V().connectedComponent().with(ConnectedComponent.propertyName, \"component\")",
                            "g.V().connectedComponent().with(ConnectedComponent.propertyName, \"component\")",
                            "g.V().connectedComponent().with(ConnectedComponent.propertyName, string0)",
                            "g.V().ConnectedComponent().With(ConnectedComponent.PropertyName, \"component\")",
                            "g.V().ConnectedComponent().With(ConnectedComponent.PropertyName, \"component\")",
                            "g.V().connectedComponent().with(ConnectedComponent.propertyName, \"component\")",
                            "g.V().connectedComponent().with(ConnectedComponent.propertyName, \"component\")",
                            "g.V().connectedComponent().with_(ConnectedComponent.propertyName, \"component\")",
                            "g.V().connected_component().with_(ConnectedComponent.property_name, 'component')"},
                    {"g.withSideEffect(\"c\", xx2).withSideEffect(\"m\", xx3).mergeE(xx1).option(Merge.onCreate, __.select(\"c\")).option(Merge.onMatch, __.select(\"m\"))",
                            null,
                            "g.withSideEffect(string0, xx2).withSideEffect(string1, xx3).mergeE(map0).option(Merge.onCreate, __.select(string0)).option(Merge.onMatch, __.select(string1))",
                            "g.WithSideEffect(\"c\", xx2).WithSideEffect(\"m\", xx3).MergeE((IDictionary<object, object>) xx1).Option(Merge.OnCreate, (ITraversal) __.Select<object>(\"c\")).Option(Merge.OnMatch, (ITraversal) __.Select<object>(\"m\"))",
                            "g.WithSideEffect(\"c\", xx2).WithSideEffect(\"m\", xx3).MergeE(xx1).Option(gremlingo.Merge.OnCreate, gremlingo.T__.Select(\"c\")).Option(gremlingo.Merge.OnMatch, gremlingo.T__.Select(\"m\"))",
                            null,
                            null,
                            null,
                            "g.with_side_effect('c', xx2).with_side_effect('m', xx3).merge_e(xx1).option(Merge.on_create, __.select('c')).option(Merge.on_match, __.select('m'))"},
                    {"g.withSack(1.0, Operator.sum).V(vid1).local(__.out(\"knows\").barrier(Barrier.normSack)).in(\"knows\").barrier().sack()",
                            null,
                            "g.withSack(number0, Operator.sum).V(vid1).local(__.out(string0).barrier(Barrier.normSack)).in(string0).barrier().sack()",
                            "g.WithSack(1.0, Operator.Sum).V(vid1).Local<object>(__.Out(\"knows\").Barrier(Barrier.NormSack)).In(\"knows\").Barrier().Sack<object>()",
                            "g.WithSack(1.0, gremlingo.Operator.Sum).V(vid1).Local(gremlingo.T__.Out(\"knows\").Barrier(gremlingo.Barrier.NormSack)).In(\"knows\").Barrier().Sack()",
                            null,
                            null,
                            "g.withSack(1.0, Operator.sum).V(vid1).local(__.out(\"knows\").barrier(Barrier.normSack)).in_(\"knows\").barrier().sack()",
                            "g.with_sack(1.0, Operator.sum_).V(vid1).local(__.out('knows').barrier(Barrier.norm_sack)).in_('knows').barrier().sack()",},
                    {"g.V(1, 2, 3)",
                            null,
                            "g.V(number0, number1, number2)",
                            null,
                            null,
                            null,
                            null,
                            null,
                            null},
                    {"g.io(\"data/tinkerpop-modern.xml\").with(IO.reader, IO.graphml).read()",
                            null,
                            "g.io(string0).with(IO.reader, IO.graphml).read()",
                            "g.Io<object>(\"data/tinkerpop-modern.xml\").With(IO.Reader, IO.GraphML).Read()",
                            "g.Io(\"data/tinkerpop-modern.xml\").With(gremlingo.IO.Reader, gremlingo.IO.Graphml).Read()",
                            null,
                            null,
                            "g.io(\"data/tinkerpop-modern.xml\").with_(IO.reader, IO.graphml).read()",
                            "g.io('data/tinkerpop-modern.xml').with_(IO.reader, IO.graphml).read()"},
                    {"g.V().limit(1)",
                            null,
                            "g.V().limit(number0)",
                            "g.V().Limit<object>(1)",
                            "g.V().Limit(1)",
                            null,
                            null,
                            null,
                            null},
                    {"g.V().group()",
                            null,
                            null,
                            "g.V().Group<object, object>()",
                            "g.V().Group()",
                            null,
                            null,
                            null,
                            null},
                    {"g.V().project(\"k\", \"v\").by().by(fold())",
                            "g.V().project(\"k\", \"v\").by().by(__.fold())",
                            "g.V().project(string0, string1).by().by(__.fold())",
                            "g.V().Project<object>(\"k\", \"v\").By().By(__.Fold<object>())",
                            "g.V().Project(\"k\", \"v\").By().By(gremlingo.T__.Fold())",
                            "g.V().project(\"k\", \"v\").by().by(__.fold())",
                            "g.V().project(\"k\", \"v\").by().by(__.fold())",
                            "g.V().project(\"k\", \"v\").by().by(__.fold())",
                            "g.V().project('k', 'v').by().by(__.fold())"},
                    {"g.V().project(\"k\", \"v\").by().by(__.fold())",
                            null,
                            "g.V().project(string0, string1).by().by(__.fold())",
                            "g.V().Project<object>(\"k\", \"v\").By().By(__.Fold<object>())",
                            "g.V().Project(\"k\", \"v\").By().By(gremlingo.T__.Fold())",
                            null,
                            null,
                            null,
                            "g.V().project('k', 'v').by().by(__.fold())"},
                    {"g.V().project(\"k\", \"v\").by().by(__.values().fold())",
                            null,
                            "g.V().project(string0, string1).by().by(__.values().fold())",
                            "g.V().Project<object>(\"k\", \"v\").By().By(__.Values<object>().Fold())",
                            "g.V().Project(\"k\", \"v\").By().By(gremlingo.T__.Values().Fold())",
                            null,
                            null,
                            null,
                            "g.V().project('k', 'v').by().by(__.values().fold())"},
                    {"g.V().valueMap()",
                            null,
                            null,
                            "g.V().ValueMap<object, object>()",
                            "g.V().ValueMap()",
                            null,
                            null,
                            null,
                            "g.V().value_map()"},
                    {"g.V().valueMap().with(WithOptions.tokens)",
                            null,
                            null,
                            "g.V().ValueMap<object, object>().With(WithOptions.Tokens)",
                            "g.V().ValueMap().With(gremlingo.WithOptions.Tokens)", // changed behavior, was string value
                            null,
                            null,
                            "g.V().valueMap().with_(WithOptions.tokens)",
                            "g.V().value_map().with_(WithOptions.tokens)"},
                    {"g.V().limit(1L)",
                            "g.V().limit(1l)",
                            "g.V().limit(long0)",
                            "g.V().Limit<object>(1l)",
                            "g.V().Limit(1)",
                            "g.V().limit(1l)",
                            "g.V().limit(1l)",
                            "g.V().limit(1)",
                            "g.V().limit(long(1))"},
                    {"g.V().limit(x)",
                            null,
                            null,
                            "g.V().Limit<object>(x)",
                            "g.V().Limit(x)",
                            null,
                            null,
                            null,
                            null},
                    {"g.mergeV([name:\"alice\", T.label:\"person\"]).option(Merge.onCreate, [age:Cardinality.single(81)])",
                            null,
                            "g.mergeV(map0).option(Merge.onCreate, map1)",
                            "g.MergeV((IDictionary<object, object>) new Dictionary<object, object> {{ \"name\", \"alice\" }, { T.Label, \"person\" }}).Option(Merge.OnCreate, (IDictionary<object, object>) new Dictionary<object, object> {{ \"age\", CardinalityValue.Single(81) }})",
                            "g.MergeV(map[interface{}]interface{}{\"name\": \"alice\", gremlingo.T.Label: \"person\" }).Option(gremlingo.Merge.OnCreate, map[interface{}]interface{}{\"age\": gremlingo.CardinalityValue.Single(81) })",
                            null,
                            "g.mergeV(new LinkedHashMap<Object, Object>() {{ put(\"name\", \"alice\"); put(T.label, \"person\"); }}).option(Merge.onCreate, new LinkedHashMap<Object, Object>() {{ put(\"age\", Cardinality.single(81)); }})",
                            "g.mergeV(new Map([[\"name\", \"alice\"], [T.label, \"person\"]])).option(Merge.onCreate, new Map([[\"age\", CardinalityValue.single(81)]]))",
                            "g.merge_v({ 'name': 'alice', T.label: 'person' }).option(Merge.on_create, { 'age': CardinalityValue.single(81) })"},
                    {"g.mergeV([name:\"alice\", (T.label):\"person\"]).option(Merge.onCreate, [age:Cardinality.single(81)])",
                            null,
                            "g.mergeV(map0).option(Merge.onCreate, map1)",
                            "g.MergeV((IDictionary<object, object>) new Dictionary<object, object> {{ \"name\", \"alice\" }, { T.Label, \"person\" }}).Option(Merge.OnCreate, (IDictionary<object, object>) new Dictionary<object, object> {{ \"age\", CardinalityValue.Single(81) }})",
                            "g.MergeV(map[interface{}]interface{}{\"name\": \"alice\", gremlingo.T.Label: \"person\" }).Option(gremlingo.Merge.OnCreate, map[interface{}]interface{}{\"age\": gremlingo.CardinalityValue.Single(81) })",
                            null,
                            "g.mergeV(new LinkedHashMap<Object, Object>() {{ put(\"name\", \"alice\"); put(T.label, \"person\"); }}).option(Merge.onCreate, new LinkedHashMap<Object, Object>() {{ put(\"age\", Cardinality.single(81)); }})",
                            "g.mergeV(new Map([[\"name\", \"alice\"], [T.label, \"person\"]])).option(Merge.onCreate, new Map([[\"age\", CardinalityValue.single(81)]]))",
                            "g.merge_v({ 'name': 'alice', T.label: 'person' }).option(Merge.on_create, { 'age': CardinalityValue.single(81) })"},
                    {"g.V().toList()",
                            null,
                            null,
                            "g.V().ToList()",
                            "g.V().ToList()",
                            null,
                            null,
                            null,
                            "g.V().to_list()"},
                    {"g.V().iterate()",
                            null,
                            null,
                            "g.V().Iterate()",
                            "g.V().Iterate()",
                            null,
                            null,
                            null,
                            null},
                    {"g.tx().commit()",
                            null,
                            null,
                            "g.Tx().Commit()",
                            "g.Tx().Commit()",
                            null,
                            null,
                            null,
                            null},
                    {"g.call(\"--list\").with(\"service\", __.constant(\"tinker.search\"))",
                            "g.call(\"--list\").with(\"service\", __.constant(\"tinker.search\"))",
                            "g.call(string0).with(string1, __.constant(string2))",
                            "g.Call<object>((string) \"--list\").With(\"service\", __.Constant<object>(\"tinker.search\"))",
                            "g.Call(\"--list\").With(\"service\", gremlingo.T__.Constant(\"tinker.search\"))",
                            "g.call(\"--list\").with(\"service\", __.constant(\"tinker.search\"))",
                            "g.call(\"--list\").with(\"service\", __.constant(\"tinker.search\"))",
                            "g.call(\"--list\").with_(\"service\", __.constant(\"tinker.search\"))",
                            "g.call('--list').with_('service', __.constant('tinker.search'))"},
            });
        }

        public TranslationTest(final String query, final String expectedForLang,
                               final String expectedForAnonymized,
                               final String expectedForDotNet,
                               final String expectedForGo,
                               final String expectedForGroovy,
                               final String expectedForJava,
                               final String expectedForJavascript,
                               final String expectedForPython) {
            this.query = query;
            this.expectedForLang = expectedForLang != null ? expectedForLang : query;
            this.expectedForAnonymized = expectedForAnonymized != null ? expectedForAnonymized : query;
            this.expectedForDotNet = expectedForDotNet != null ? expectedForDotNet : query;
            this.expectedForGo = expectedForGo != null ? expectedForGo : query;
            this.expectedForGroovy = expectedForGroovy != null ? expectedForGroovy : query;
            this.expectedForJava = expectedForJava != null ? expectedForJava : query;
            this.expectedForJavascript = expectedForJavascript != null ? expectedForJavascript : query;
            this.expectedForPython = expectedForPython != null ? expectedForPython : query;
        }

        @Test
        public void shouldTranslateForLang() {
            final String translatedQuery = GremlinTranslator.translate(query, Translator.LANGUAGE).getTranslated();
            assertEquals(expectedForLang, translatedQuery);
        }

        @Test
        public void shouldTranslateForAnonymized() {
            final String translatedQuery = GremlinTranslator.translate(query, Translator.ANONYMIZED).getTranslated();
            assertEquals(expectedForAnonymized, translatedQuery);
        }

        @Test
        public void shouldTranslateForDotNet() {
            try {
                final String translatedQuery = GremlinTranslator.translate(query, "g", Translator.DOTNET).getTranslated();
                assertEquals(expectedForDotNet, translatedQuery);
            } catch (TranslatorException e) {
                assertThat(e.getMessage(), startsWith(expectedForDotNet));
            }
        }

        @Test
        public void shouldTranslateForGo() {
            try {
                final String translatedQuery = GremlinTranslator.translate(query, "g", Translator.GO).getTranslated();
                assertEquals(expectedForGo, translatedQuery);
            } catch (TranslatorException e) {
                assertThat(e.getMessage(), startsWith(expectedForGo));
            }
        }

        @Test
        public void shouldTranslateForGroovy() {
            try {
                final String translatedQuery = GremlinTranslator.translate(query, "g", Translator.GROOVY).getTranslated();
                assertEquals(expectedForGroovy, translatedQuery);
            } catch (TranslatorException e) {
                assertThat(e.getMessage(), startsWith(expectedForGroovy));
            }
        }

        @Test
        public void shouldTranslateForJava() {
            try {
                final String translatedQuery = GremlinTranslator.translate(query, "g", Translator.JAVA).getTranslated();
                assertEquals(expectedForJava, translatedQuery);
            } catch (TranslatorException e) {
                assertThat(e.getMessage(), startsWith(expectedForJava));
            }
        }

        @Test
        public void shouldTranslateForJavascript() {
            try {
                final String translatedQuery = GremlinTranslator.translate(query, "g", Translator.JAVASCRIPT).getTranslated();
                assertEquals(expectedForJavascript, translatedQuery);
            } catch (TranslatorException e) {
                assertThat(e.getMessage(), startsWith(expectedForJavascript));
            }
        }

        @Test
        public void shouldTranslateForPython() {
            try {
                final String translatedQuery = GremlinTranslator.translate(query, "g", Translator.PYTHON).getTranslated();
                assertEquals(expectedForPython, translatedQuery);
            } catch (TranslatorException e) {
                assertThat(e.getMessage(), startsWith(expectedForPython));
            }
        }
    }
}