"""Provides diff testing functionality."""

load("@bazel_skylib//rules:diff_test.bzl", skylib_diff_test = "diff_test")

def _loc(target):
    return "$(location {})".format(target)

def diff_test(name, bin, input, output, data = [], args = [], tags = []):
    """Generates a diff test target.

    Args:
      name: Test target name.
      bin: Binary that should be executed to generate the output.
      input: Input to pass through the stdin.
      output: Expected output.
      data: General data.
      args: Arguments that are passed to `bin`.
      tags: Test tags.
    """

    actual_output = "{}_actual-output".format(name)
    actual_output_label = ":{}".format(actual_output)

    native.genrule(
        name = actual_output,
        srcs = data + [input],
        outs = ["{}.txt".format(actual_output)],
        cmd = "{} {} < {} > $@".format(_loc(bin), " ".join(args), _loc(input)),
        tools = [bin],
    )

    native.sh_binary(
        name = "{}.code".format(name),
        srcs = ["//lib:code-diff.sh"],
        data = [output, actual_output_label],
        args = [_loc(output), _loc(actual_output_label)],
    )

    skylib_diff_test(
        name = name,
        file1 = actual_output_label,
        file2 = output,
        tags = tags,
    )
